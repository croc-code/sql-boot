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

package com.github.mgramin.sqlboot.model.uri

import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.model.uri.wrappers.JsonWrapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * Created by maksim on 12.06.16.
 */
class DbUriTest {

    @Test
    fun createAllTableFromAllSchema() {
        assertEqualsUri("prod/table/*", "DbUri{type='table', path=[*], params={}}")
        assertEqualsUri("prod/table/", "DbUri{type='table', path=[], params={}}")
    }

    @Test
    fun createAllTableFromSchema() {
        assertEqualsUri("prod/table/hr", "DbUri{type='table', path=[hr], params={}}")
        assertEqualsUri("prod/table/hr.*", "DbUri{type='table', path=[hr, *], params={}}")
    }

    @Test
    fun createAllTableWithChildObjectsFromSchema() {
        assertEqualsUri("prod/table/hr.*", "DbUri{type='table', path=[hr, *], params={}}")
    }

    @Test
    fun dropAllTableFromSchema() {
        assertEqualsUri("prod/table/hr.*/drop", "DbUri{type='table', path=[hr, *], params={}}")
    }

    @Test
    fun createColumnsForTable() {
        assertEqualsUri("prod/column/hr.persons.*name",
                "DbUri{type='column', path=[hr, persons, *name], params={}}")
    }

    @Test
    fun dropColumnFromTable() {
        assertEqualsUri("prod/column/hr.persons.name/drop",
                "DbUri{type='column', path=[hr, persons, name], params={}}")
    }

    @Test
    fun createAllFkForTable() {
        assertEqualsUri("prod/fk/hr.employees.*",
                "DbUri{type='fk', path=[hr, employees, *], params={}}")
    }

    @Test
    fun dropAllFkFromTable() {
        assertEqualsUri("prod/fk/hr.employees.*/drop",
                "DbUri{type='fk', path=[hr, employees, *], params={}}")
    }

    @Test
    fun disableAllFkFromTable() {
        assertEqualsUri("prod/fk/hr.employees.*/disable",
                "DbUri{type='fk', path=[hr, employees, *], params={}}")
    }

    @Test
    fun disableAllFkFromSchema() {
        assertEqualsUri("prod/fk/hr.*.*/disable",
                "DbUri{type='fk', path=[hr, *, *], params={}}")
    }

    @Test
    fun testDefaultActionIsCreate() {
        assertEqualsUri("prod/fk/hr.*.*",
                "DbUri{type='fk', path=[hr, *, *], params={}}")
    }

    @Test
    fun testParams() {
        assertEqualsUri("prod/t/hr?@table_comment=big_table",
                "DbUri{type='t', path=[hr], params={@table_comment=big_table}}")
        assertEqualsUri("prod/table/hr?@table_comment=big_table",
                "DbUri{type='table', path=[hr], params={@table_comment=big_table}}")
    }

    @Test
    fun testAction() = assertEquals("count", DbUri("prod/table/hr.p*/count?limit=10").action())


    @ParameterizedTest
    @CsvSource(
            "prod/table/hr.persons?page=1,10#1#10",
            "prod/table/hr.persons?page=2,10#2#10",
            "prod/table/hr.persons?page=1,5#1#5",
            "prod/table/hr.persons?page=2,5#2#5",
            "prod/table/hr.persons#1#10",
            delimiter = '#')
    fun testPageParameters(uri: String, number: Int, size: Int) = assertEquals(number, DbUri(uri).pageNumber())

    @ParameterizedTest
    @CsvSource(
            "{}#prod/table/hr.persons",
            "{age=desc}#prod/table/hr.persons?orderby=age-desc",
            "{age=asc}#prod/table/hr.persons?orderby=age-asc",
            "{age=asc}#prod/table/hr.persons?orderby=age",
            "{age=desc, name=asc}#prod/table/hr.persons?orderby=age-desc,name-asc",
            delimiter = '#')
    fun testOrderParameters(expected: String, uri: String) {
        assertEquals(expected, DbUri(uri).orderedColumns().toSortedMap().toString())
    }

    @Test
    fun testFilter() {
        println(DbUri("""prod/table/hr.persons?filter={"test":"test"}""").filter())
    }

    private fun assertEqualsUri(uriString: String, jsonExpected: String) {
        val uri = DbUri(uriString)
        assertEquals(uriString, uri.toString())
        assertEquals(JsonWrapper(DbUri(uriString)).toString(), jsonExpected)
    }

}