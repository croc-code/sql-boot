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
        test("table/*", "DbUri{type='table', path=[*], recursive=false, params={}}")
    }

    @Test
    fun createAllTableFromSchema() {
        test("table/hr", "DbUri{type='table', path=[hr], recursive=false, params={}}")
        test("table/hr.*", "DbUri{type='table', path=[hr, *], recursive=false, params={}}")
    }

    @Test
    fun createAllTableWithChildObjectsFromSchema() {
        test("table/hr.*/", "DbUri{type='table', path=[hr, *], recursive=true, params={}}")
    }

    @Test
    fun dropAllTableFromSchema() {
        test("table/hr.*/drop", "DbUri{type='table', path=[hr, *], recursive=false, params={}}")
    }

    @Test
    fun createColumnsForTable() {
        test("column/hr.persons.*name",
                "DbUri{type='column', path=[hr, persons, *name], recursive=false, params={}}")
    }

    @Test
    fun dropColumnFromTable() {
        test("column/hr.persons.name/drop",
                "DbUri{type='column', path=[hr, persons, name], recursive=false, params={}}")
    }

    @Test
    fun createAllFkForTable() {
        test("fk/hr.employees.*",
                "DbUri{type='fk', path=[hr, employees, *], recursive=false, params={}}")
    }

    @Test
    fun dropAllFkFromTable() {
        test("fk/hr.employees.*/drop",
                "DbUri{type='fk', path=[hr, employees, *], recursive=false, params={}}")
    }

    @Test
    fun disableAllFkFromTable() {
        test("fk/hr.employees.*/disable",
                "DbUri{type='fk', path=[hr, employees, *], recursive=false, params={}}")
    }

    @Test
    fun disableAllFkFromSchema() {
        test("fk/hr.*.*/disable",
                "DbUri{type='fk', path=[hr, *, *], recursive=false, params={}}")
    }

    @Test
    fun testDefaultActionIsCreate() {
        test("fk/hr.*.*",
                "DbUri{type='fk', path=[hr, *, *], recursive=false, params={}}")
    }

    @Test
    fun testParams() {
        test("t/hr?@table_comment=big_table",
                "DbUri{type='t', path=[hr], recursive=false, params={@table_comment=big_table}}")
        test("table/hr?@table_comment=big_table",
                "DbUri{type='table', path=[hr], recursive=false, params={@table_comment=big_table}}")
    }

    @Test
    fun testAction() {
        val dbUri = DbUri("table/hr.p*/count?limit=10")
        assertEquals("count", dbUri.action())
    }

    @ParameterizedTest
    @CsvSource(
            "table/hr.persons?page=1,10#1#10",
            "table/hr.persons?page=2,10#2#10",
            "table/hr.persons?page=1,5#1#5",
            "table/hr.persons?page=2,5#2#5",
            "table/hr.persons#1#10",
            delimiter = '#')
    fun testPageParameters(uri: String, expectedPageNumber: Int, expectedPageSize: Int) {
        val dbUri = DbUri(uri)
        assertEquals(expectedPageNumber, dbUri.pageNumber())
        assertEquals(expectedPageSize, dbUri.pageSize())
    }

    @ParameterizedTest
    @CsvSource(
            "{}#table/hr.persons",
            "{age=desc}#table/hr.persons?orderby=age-desc",
            "{age=asc}#table/hr.persons?orderby=age-asc",
            "{age=asc}#table/hr.persons?orderby=age",
            "{age=desc, name=asc}#table/hr.persons?orderby=age-desc,name-asc",
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