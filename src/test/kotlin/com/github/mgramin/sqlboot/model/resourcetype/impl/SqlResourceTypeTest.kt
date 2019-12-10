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
        db.properties = mapOf(
                "sql_dialect" to "h2",
                "jdbc_url" to "jdbc:h2:mem:;INIT=RUNSCRIPT FROM 'classpath:schema.sql';",
                "jdbc_driver_class_name" to "org.h2.Driver"
        )
    }

    private val getAllTablesSQL =
            """/* { "name": "table" } */
              |select table_schema
              |     , table_name
              |  from information_schema.tables""".trimMargin()

    private val getAllColumnsSQL =
            """/* { "name": "column" } */
              |select table_schema
              |     , table_name
              |     , column_name
              |  from information_schema.columns""".trimMargin()

    val getAllProcesses =
            """/* { "name": "process", "executor": "http" } */
              |select pid as "@pid"
              |     , name
              |     , total_size
              |  from processes
              | limit 5""".trimMargin()

    @Test
    fun name() =
            assertEquals("table",
                    SqlResourceType("test", getAllTablesSQL, listOf(db), listOf(FakeDialect())).name())


    @Test
    fun aliases() =
            assertEquals(arrayListOf("table"),
                    SqlResourceType("test", getAllTablesSQL, listOf(db), listOf(FakeDialect())).aliases())

    @Test
    fun readAllTables() {
        StepVerifier
                .create(createType(getAllTablesSQL).read(DbUri("prod/table")))
                .expectNextCount(46)
                .verifyComplete()
    }

    @Test
    fun readAllColumns() {
        StepVerifier
                .create(createType(getAllColumnsSQL).read(DbUri("prod/column")))
                .expectNextCount(360)
                .verifyComplete()
    }

    @Test
    fun path() = assertEquals(listOf("table_schema"), createType(getAllColumnsSQL).path())

    @Test
    fun metaData() = assertEquals(4, createType(getAllColumnsSQL).metaData(FakeUri()).count())

    @Test
    fun toJson() {
        val json = createType(getAllColumnsSQL).toJson()
        assertEquals("column", json.get("name").asText())
        assertEquals("[column]", json.get("aliases").asText())
    }

    private fun createType(sql: String) =
            SqlResourceType(
                    name = "test",
                    sql = sql,
                    endpoints = listOf(db),
                    dialects = listOf(FakeDialect()))

}