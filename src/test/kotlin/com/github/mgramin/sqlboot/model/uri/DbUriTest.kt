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
        test("prod/table/*", "DbUri{type='table', path=[*], params={}}")
        test("prod/table/", "DbUri{type='table', path=[], params={}}")
    }

    @Test
    fun createAllTableFromSchema() {
        test("prod/table/hr", "DbUri{type='table', path=[hr], params={}}")
        test("prod/table/hr.*", "DbUri{type='table', path=[hr, *], params={}}")
    }

    @Test
    fun createAllTableWithChildObjectsFromSchema() {
        test("prod/table/hr.*", "DbUri{type='table', path=[hr, *], params={}}")
    }

    @Test
    fun dropAllTableFromSchema() {
        test("prod/table/hr.*/drop", "DbUri{type='table', path=[hr, *], params={}}")
    }

    @Test
    fun createColumnsForTable() {
        test("prod/column/hr.persons.*name",
                "DbUri{type='column', path=[hr, persons, *name], params={}}")
    }

    @Test
    fun dropColumnFromTable() {
        test("prod/column/hr.persons.name/drop",
                "DbUri{type='column', path=[hr, persons, name], params={}}")
    }

    @Test
    fun createAllFkForTable() {
        test("prod/fk/hr.employees.*",
                "DbUri{type='fk', path=[hr, employees, *], params={}}")
    }

    @Test
    fun dropAllFkFromTable() {
        test("prod/fk/hr.employees.*/drop",
                "DbUri{type='fk', path=[hr, employees, *], params={}}")
    }

    @Test
    fun disableAllFkFromTable() {
        test("prod/fk/hr.employees.*/disable",
                "DbUri{type='fk', path=[hr, employees, *], params={}}")
    }

    @Test
    fun disableAllFkFromSchema() {
        test("prod/fk/hr.*.*/disable",
                "DbUri{type='fk', path=[hr, *, *], params={}}")
    }

    @Test
    fun testDefaultActionIsCreate() {
        test("prod/fk/hr.*.*",
                "DbUri{type='fk', path=[hr, *, *], params={}}")
    }

    @Test
    fun testParams() {
        test("prod/t/hr?@table_comment=big_table",
                "DbUri{type='t', path=[hr], params={@table_comment=big_table}}")
        test("prod/table/hr?@table_comment=big_table",
                "DbUri{type='table', path=[hr], params={@table_comment=big_table}}")
    }

    @Test
    fun testAction() {
        val dbUri = DbUri("prod/table/hr.p*/count?limit=10")
        assertEquals("count", dbUri.action())
    }

    @ParameterizedTest
    @CsvSource(
            "prod/table/hr.persons?page=1,10#1#10",
            "prod/table/hr.persons?page=2,10#2#10",
            "prod/table/hr.persons?page=1,5#1#5",
            "prod/table/hr.persons?page=2,5#2#5",
            "prod/table/hr.persons#1#10",
            delimiter = '#')
    fun testPageParameters(uri: String, expectedPageNumber: Int, expectedPageSize: Int) {
        val dbUri = DbUri(uri)
        assertEquals(expectedPageNumber, dbUri.pageNumber())
        assertEquals(expectedPageSize, dbUri.pageSize())
    }

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

    private fun test(uriString: String, jsonExpected: String) {
        val uri = DbUri(uriString)
        assertEquals(uriString, uri.toString())
        assertEquals(JsonWrapper(DbUri(uriString)).toString(), jsonExpected)
    }

}