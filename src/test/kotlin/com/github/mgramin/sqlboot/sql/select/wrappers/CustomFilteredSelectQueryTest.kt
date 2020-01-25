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

package com.github.mgramin.sqlboot.sql.select.wrappers

import com.github.mgramin.sqlboot.sql.select.Column
import com.github.mgramin.sqlboot.sql.select.SelectQuery
import com.github.mgramin.sqlboot.sql.select.impl.FakeSelectQuery
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Test

internal class CustomFilteredSelectQueryTest {

    @Test
    fun query() {
        val columns: List<Column> = listOf(mock<Column> {
            on { name() } doReturn "size"
            on { datatype() } doReturn "float8"
        })

        val select = mock<SelectQuery> {
            on { name() } doReturn "fake_query"
            on { query() } doReturn "select * from test"
            on { columns() } doReturn columns
        }

        println(CustomFilteredSelectQuery(select,
                JsonParser().parse("""{ "size": 1234567891011 }""").asJsonObject).query())

        println(CustomFilteredSelectQuery(FakeSelectQuery(),
                JsonParser().parse("""{ "registration_date": {"start": "2019-09-05 00:00","end": "2019-09-13 23:59"}}""").asJsonObject).query())
        println(CustomFilteredSelectQuery(FakeSelectQuery(),
                JsonParser().parse("""{ "n": "John Doe", "mail": "doe@mail.com" }""").asJsonObject).query())
        println(CustomFilteredSelectQuery(FakeSelectQuery(),
                JsonParser().parse("{}").asJsonObject).query())
        println(CustomFilteredSelectQuery(FakeSelectQuery(),
                JsonObject()).query())
    }

    @Test
    fun properties() {
    }

    @Test
    fun columns() {
    }

    @Test
    fun execute() {
    }

}