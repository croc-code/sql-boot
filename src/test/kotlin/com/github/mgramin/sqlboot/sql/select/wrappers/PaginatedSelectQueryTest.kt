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

package com.github.mgramin.sqlboot.sql.select.wrappers

import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.sql.select.impl.FakeSelectQuery
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class PaginatedSelectQueryTest {

    @ParameterizedTest
    @CsvSource(
            "offset 0 limit 1,1,1",
            "offset 0 limit 2,1,2",
            "offset 0 limit 3,1,3",
            "offset 1 limit 1,2,1",
            "offset 2 limit 2,2,2",
            "offset 2 limit 1,3,1",
            "offset 3 limit 1,4,1",
            "offset 0 limit 100,1,100")
    fun query(expectedQuery: String, pageNumber: Int, pageSize: Int) {
        val paginatedSelectQuery =
                PaginatedSelectQuery(
                        origin = FakeSelectQuery(),
                        uri = DbUri("table/hr.persons?page=$pageNumber,$pageSize"),
                        template = "${"$"}{query} offset ${"$"}offset limit ${"$"}limit")
        println(paginatedSelectQuery.query())
        assertTrue(paginatedSelectQuery.query().contains(expectedQuery))
    }

    @Test
    fun columns() {
    }

    @Test
    fun execute() {

    }

}