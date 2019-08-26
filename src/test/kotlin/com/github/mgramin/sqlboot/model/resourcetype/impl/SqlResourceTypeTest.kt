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

import com.github.mgramin.sqlboot.model.connection.SimpleEndpoint
import com.github.mgramin.sqlboot.model.dialect.FakeDialect
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.model.uri.impl.FakeUri
import com.google.gson.JsonObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.StepVerifier

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 3d61942b737e9bf2768c93c6058e1ec55929b6f3 $
 * @since 0.1
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(locations = ["/test_config.xml"])
class SqlResourceTypeTest {

    private val db = SimpleEndpoint()

    init {
        db.name = "unit_test_db"
        db.host = "127.0.0.1"
        db.properties = mapOf(
                "sql_dialect" to "h2",
                "jdbc_url" to "jdbc:h2:mem:;INIT=RUNSCRIPT FROM 'classpath:schema.sql';",
                "jdbc_driver_class_name" to "org.h2.Driver"
        )
    }

    @Test
    fun name() {
        val sql = """/*
                    |  { "name": "table" }
                    |*/
                    |select *
                    |  from (select table_schema   as "@schema"
                    |             , table_name     as "@table"
                    |          from information_schema.tables)""".trimMargin()
        val name = SqlResourceType(sql, listOf(db), listOf(FakeDialect())).name()
        assertEquals("table", name)
    }

    @Test
    fun aliases() {
        val sql = """/*
                    |  { "name": "table" }
                    |*/
                    |select *
                    |  from (select table_schema   as "@schema"
                    |             , table_name     as "@table"
                    |          from information_schema.tables)""".trimMargin()
        val aliases = SqlResourceType(sql, listOf(db), listOf(FakeDialect())).aliases()
        assertEquals(arrayListOf("table"), aliases)
    }

    @Test
    fun read() {
        val sql = """/*
                    |  { "name": "table" }
                    |*/
                    |select @schema, @table
                    |  from (select table_schema   as "@schema"
                    |             , table_name     as "@table"
                    |          from information_schema.tables)""".trimMargin()
        StepVerifier
                .create(createType(sql).read(FakeUri()))
                .expectNextCount(46)
                .verifyComplete()
    }

    @Test
    fun read2() {
        val sql = """/*
                    |  { "name": "column" }
                    |*/
                    |select @schema, @table, @column
                    |  from (select table_schema    as "@schema"
                    |             , table_name      as "@table"
                    |             , column_name     as "@column"
                    |          from information_schema.columns)""".trimMargin()
        StepVerifier
                .create(createType(sql).read(FakeUri()))
                .expectNextCount(347)
                .verifyComplete()
    }

    @Test
    @Disabled
    fun read3() {
        val sql = """/* { "name": "process", "executor": "http" } */
                    |select pid as "@pid"
                    |     , name
                    |     , total_size
                    |  from processes
                    | limit 5""".trimMargin()
        StepVerifier
                .create(createType(sql).read(DbUri("prod/process")))
                .expectNextCount(5)
                .verifyComplete()
    }

    @Test
    fun path() {
        val sql = """/*
                    |  { "name": "column" }
                    |*/
                    |select table_schema  as "@schema"
                    |     , table_name    as "@table"
                    |     , column_name   as "@column"
                    |  from information_schema.columns""".trimMargin()
        assertEquals("[schema, table, column]", createType(sql).path().toString())
    }

    @Test
    fun path2() {
        val sql = """/*
                    |  { "name": "column" }
                    |*/
                    |select table_schema  as "schema"
                    |     , table_name    as "table"
                    |     , column_name   as "column"
                    |  from information_schema.columns""".trimMargin()
        assertEquals("[schema]", createType(sql).path().toString())
    }

    @Test
    fun metaData() {
        val sql = """/*
                    |  { "name": "column" }
                    |*/
                    |select table_schema  as "@schema"
                    |     , table_name    as "@table"
                    |     , column_name   as "@column"
                    |  from information_schema.columns""".trimMargin()
        assertEquals(4, createType(sql).metaData(FakeUri()).count())
    }

    @Test
    fun toJson() {
        val sql = """/*
                    |  { "name": "column" }
                    |*/
                    |select table_schema  as "@schema"
                    |     , table_name    as "@table"
                    |     , column_name   as "@column"
                    |  from information_schema.columns""".trimMargin()
        val json: JsonObject = createType(sql).toJson()
        assertEquals("column", json.get("name").asString)
        assertEquals("[column]", json.get("aliases").asString)
    }

    private fun createType(sql: String) =
            SqlResourceType(
                    sql = sql,
                    endpoints = listOf(db),
                    dialects = listOf(FakeDialect()))

}