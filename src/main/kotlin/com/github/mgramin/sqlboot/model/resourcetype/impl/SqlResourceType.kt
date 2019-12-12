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

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
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
import com.github.mgramin.sqlboot.sql.select.wrappers.exec.JdbcSelectQuery
import com.github.mgramin.sqlboot.sql.select.wrappers.FilteredSelectQuery
import com.github.mgramin.sqlboot.sql.select.wrappers.GrafanaSelectQuery
import com.github.mgramin.sqlboot.sql.select.wrappers.OrderedSelectQuery
import com.github.mgramin.sqlboot.sql.select.wrappers.PaginatedSelectQuery
import com.github.mgramin.sqlboot.sql.select.wrappers.TypedSelectQuery
import com.github.mgramin.sqlboot.template.generator.impl.JinjaTemplateGenerator
import reactor.core.publisher.Flux

/**
 * Created by MGramin on 12.07.2017.
 */
class SqlResourceType(
        name: String,
        sql: String,
        private val endpoints: List<Endpoint>,
        private val dialects: List<Dialect>
) : ResourceType {

    private val simpleSelectQuery = SimpleSelectQuery(name, JinjaTemplateGenerator(sql))

    override fun aliases(): List<String> =
            if (simpleSelectQuery.properties().containsKey("name")) {
                listOf(simpleSelectQuery.properties()["name"]!!)
            } else {
                listOf(simpleSelectQuery.name())
            }

    override fun path(): List<String> = listOf(simpleSelectQuery.columns().first().name())

    override fun read(uri: Uri): Flux<DbResource> = Flux
            .merge(endpoints
                    .map { connection ->
                        return@map createQuery(uri, connection)
                                .execute(hashMapOf("uri" to uri))
                                .map<Map<String, Any>?> {
                                    val toMutableMap = it.toMutableMap()
                                    toMutableMap["endpoint"] = connection.name()
                                    toMutableMap
                                }
                    }
                    .toList())
            .map { o ->
                val path = o!!.entries.map { it.value.toString() }
                val headers = o.entries
                        .map { it.key to it.value }
                        .toMap()
                val name = if (path.isEmpty()) {
                    headers.asSequence().first().key
                } else {
                    path[path.size - 1]
                }
                DbResourceImpl(name, this, DbUri(headers["endpoint"].toString(), this.name(), path), headers) as DbResource
            }

    override fun metaData(uri: Uri): List<Metadata> {
        val endpoint = endpoints
                .filter { it.properties().containsKey("tags") && it.properties()["tags"] == "history" }
                .ifEmpty { endpoints.filter { it.properties().containsKey("default") } }
                .ifEmpty { endpoints }
                .first()
        val selectQuery = createQuery(uri, endpoint)
        return listOf(Metadata("endpoint", """{"label": "Cluster", "description": "Source cluster", "visible": true, "sortable": false}""")) +
                selectQuery
                        .columns()
                        .map { Metadata(it.name(), it.datatype(), it.comment()) }
    }

    override fun toJson(): JsonNode {
        val jsonNode: ObjectNode = ObjectMapper().createObjectNode()
        jsonNode.put("name", name())
        jsonNode.put("aliases", aliases().toString())
        jsonNode.put("query", simpleSelectQuery.query())

        val node: JsonNode = ObjectMapper().valueToTree(simpleSelectQuery.properties())
        jsonNode.put("properties", node)
        val jsonArray = ObjectMapper().createArrayNode()
        metaData(FakeUri()).forEach { jsonArray.add(it.toJson()) }
        jsonNode.put("metadata", jsonArray)
        return jsonNode
    }

    private fun createQuery(uri: Uri, endpoint: Endpoint): SelectQuery {
        val dialect = dialects.first { it.name() == endpoint.properties()["sql_dialect"].toString() }
        return JdbcSelectQuery(
                PaginatedSelectQuery(
                        OrderedSelectQuery(
                                CustomFilteredSelectQuery(
                                        FilteredSelectQuery(
                                                TypedSelectQuery(
                                                        GrafanaSelectQuery(
                                                                simpleSelectQuery),
                                                        dataSource = endpoint.getDataSource()),
                                                uri.path()),
                                        uri.filter()),
                                uri.orderedColumns()),
                        uri,
                        dialect.paginationQueryTemplate()),
                dataSource = endpoint.getDataSource())
    }

}
