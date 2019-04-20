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

import com.github.mgramin.sqlboot.model.connection.SimpleDbConnection
import com.github.mgramin.sqlboot.model.dialect.FakeDialect
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.model.uri.impl.FakeUri
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.core.io.FileSystemResource
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

    private val db = SimpleDbConnection()

    init {
        db.name = "unit_test_db"
        db.dialect = "h2"
        db.baseFolder = FileSystemResource("conf/h2/database")
        db.url = "jdbc:h2:mem:;INIT=RUNSCRIPT FROM 'classpath:schema.sql';"
        db.paginationQueryTemplate = "${'$'}{query} offset ${'$'}{uri.pageSize()*(uri.pageNumber()-1)} limit ${'$'}{uri.pageSize()}"
    }

    @Test
    fun name() {
        val name = SqlResourceType(listOf("table", "tbl", "t"), "", listOf(db), listOf(FakeDialect())).name()
        assertEquals("table", name)
    }

    @Test
    fun aliases() {
        val aliases = SqlResourceType(listOf("table", "tbl", "t"), "", listOf(db), listOf(FakeDialect())).aliases()
        assertEquals(arrayListOf("table", "tbl", "t"), aliases)
    }

    @Test
    fun read() {
        val sql = """select *
                    |  from (select table_schema   as "@schema"
                    |             , table_name     as "@table"
                    |          from information_schema.tables)""".trimMargin()
        StepVerifier
                .create(createSqlResourceType(sql).read(FakeUri()))
                .expectNextCount(46)
                .verifyComplete()
    }

    @Test
    fun read2() {
        val sql = """select *
                    |  from (select table_schema    as "@schema"
                    |             , table_name      as "@table"
                    |             , column_name     as "@column"
                    |          from information_schema.columns)""".trimMargin()
        StepVerifier
                .create(createSqlResourceType(sql).read(FakeUri()))
                .expectNextCount(347)
                .verifyComplete()
    }

    @Test
    fun read3() {
        val sql = """/* { "executor": "http" } */
                    |select pid as "@pid"
                    |     , name
                    |     , total_size
                    |  from processes
                    | limit 5""".trimMargin()
        StepVerifier
                .create(createSqlResourceType(sql).read(DbUri("prod/process")))
                .expectNextCount(5)
                .verifyComplete()
    }

    @Test
    fun path() {
        val sql = """select @schema
                    |     , @table
                    |     , @column
                    |  from (select table_schema    as "@schema"
                    |             , table_name      as "@table"
                    |             , column_name     as "@column"
                    |          from information_schema.columns)""".trimMargin()
        val type =
                SqlResourceType(
                        aliases = arrayListOf("column"),
                        sql = sql,
                        connections = listOf(db),
                        dialects = emptyList())
        assertEquals("[schema, table, column]", type.path().toString())
    }

    @Test
    fun metaData() {
    }

    private fun createSqlResourceType(sql: String) =
            SqlResourceType(
                    aliases = arrayListOf("table"),
                    sql = sql,
                    connections = listOf(db),
                    dialects = listOf(FakeDialect()))

}