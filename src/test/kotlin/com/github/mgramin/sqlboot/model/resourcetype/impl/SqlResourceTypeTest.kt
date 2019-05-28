/*
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2019 Maksim Gramin
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
        db.properties = """
            {
                "sql.dialect": "h2",
                "jdbc.url": "jdbc:h2:mem:;INIT=RUNSCRIPT FROM 'classpath:schema.sql';",
                "jdbc.driver.class.name": "org.h2.Driver"
            }
            """.trimIndent()
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
                    |select *
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
                    |select *
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