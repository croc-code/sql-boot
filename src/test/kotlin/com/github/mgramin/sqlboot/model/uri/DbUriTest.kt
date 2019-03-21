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
        test("prod/table/*", "DbUri{type='table', path=[*], params={}}")
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