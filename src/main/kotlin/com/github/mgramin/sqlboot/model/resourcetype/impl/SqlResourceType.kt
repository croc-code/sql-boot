/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2019, CROC Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.mgramin.sqlboot.model.resourcetype.impl

import com.github.mgramin.sqlboot.model.connection.Endpoint
import com.github.mgramin.sqlboot.model.dialect.Dialect
import com.github.mgramin.sqlboot.model.resource.DbResource
import com.github.mgramin.sqlboot.model.resource.impl.DbResourceImpl
import com.github.mgramin.sqlboot.model.resourcetype.Metadata
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.uri.Uri
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.model.uri.impl.FakeUri
import com.github.mgramin.sqlboot.sql.select.SelectQuery
import com.github.mgramin.sqlboot.sql.select.impl.SimpleSelectQuery
import com.github.mgramin.sqlboot.sql.select.wrappers.CustomFilteredSelectQuery
import com.github.mgramin.sqlboot.sql.select.wrappers.FilteredSelectQuery
import com.github.mgramin.sqlboot.sql.select.wrappers.GrafanaSelectQuery
import com.github.mgramin.sqlboot.sql.select.wrappers.JdbcSelectQuery
import com.github.mgramin.sqlboot.sql.select.wrappers.OrderedSelectQuery
import com.github.mgramin.sqlboot.sql.select.wrappers.PaginatedSelectQuery
import com.github.mgramin.sqlboot.template.generator.impl.JinjaTemplateGenerator
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.apache.commons.lang3.StringUtils.strip
import reactor.core.publisher.Flux

/**
 * Created by MGramin on 12.07.2017.
 */
class SqlResourceType(
        sql: String,
        private val endpoints: List<Endpoint>,
        private val dialects: List<Dialect>
) : ResourceType {

    private val simpleSelectQuery = SimpleSelectQuery(JinjaTemplateGenerator(sql))

    override fun aliases(): List<String> {
        return listOf(simpleSelectQuery.properties()["name"]!!)
    }

    override fun path(): List<String> {
        return simpleSelectQuery.columns().keys
                .filter { v -> v.startsWith("@") }
                .map { v -> strip(v, "@") }
                .ifEmpty { listOf(simpleSelectQuery.columns().keys.first()) }
    }

    override fun read(uri: Uri): Flux<DbResource> {
        val specificDialect = simpleSelectQuery.properties()["dialect"]
        return Flux.merge(
                endpoints
                        .map { connection ->
                            return@map createQuery(uri, connection, specificDialect
                                    ?: connection.properties()["sql_dialect"].toString()).execute(hashMapOf("uri" to uri))
                                    .map<Map<String, Any>?> {
                                        val toMutableMap = it.toMutableMap()
                                        toMutableMap["endpoint"] = connection.name()
                                        toMutableMap
                                    }
                        }
                        .toList())
                .map { o ->
                    val path = o!!.entries
                            .filter { v -> v.key.startsWith("@") }
                            .map { it.value.toString() }
                    val headers = o.entries
                            .map { strip(it.key, "@") to it.value }
                            .toMap()
                    val name = if (path.isEmpty()) {
                        headers.asSequence().first().key
                    } else {
                        path[path.size - 1]
                    }
                    DbResourceImpl(name, this, DbUri(headers["endpoint"].toString(), this.name(), path), headers) as DbResource
                }
    }

    override fun metaData(uri: Uri): List<Metadata> =
            listOf(Metadata("endpoint", """{"label": "Cluster", "description": "Source cluster", "visible": true, "sortable": false}""")) +
                    simpleSelectQuery
                            .columns()
                            .map { Metadata(it.key, it.value) }

    override fun toJson(): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("name", name())
        jsonObject.addProperty("aliases", aliases().toString())
        jsonObject.addProperty("query", simpleSelectQuery.query())
        jsonObject.add("properties", Gson().toJsonTree(simpleSelectQuery.properties()))
        val jsonArray = JsonArray()
        metaData(FakeUri()).forEach { jsonArray.add(it.toJson()) }
        jsonObject.add("metadata", jsonArray)
        return jsonObject
    }

    private fun createQuery(uri: Uri, endpoint: Endpoint, dialect: String): SelectQuery {
        val paginationQueryTemplate = dialects.first { it.name() == dialect }.paginationQueryTemplate()
        return JdbcSelectQuery(
                GrafanaSelectQuery(
                        PaginatedSelectQuery(
                                OrderedSelectQuery(
                                        CustomFilteredSelectQuery(
                                                FilteredSelectQuery(
                                                        simpleSelectQuery, uri.path()),
                                                uri.filter()),
                                        uri.orderedColumns()),
                                uri,
                                paginationQueryTemplate)),
                dataSource = endpoint.getDataSource())
    }

}
