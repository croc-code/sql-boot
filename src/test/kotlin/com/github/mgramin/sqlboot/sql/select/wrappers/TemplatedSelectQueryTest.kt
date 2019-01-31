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

import com.github.mgramin.sqlboot.model.connection.DbConnection
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.sql.select.impl.FakeSelectQuery
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.sql.DataSource

@ExtendWith(SpringExtension::class)
@ContextConfiguration(locations = ["/test_config.xml"])
internal class TemplatedSelectQueryTest {

    @Autowired
    internal var dataSource: DataSource? = null

    @Test
    fun query() {
    }

    @Test
    fun columns() {
    }

    @ParameterizedTest
    @CsvSource(
            "1,1,1",
            "2,1,2",
            "3,1,3",
            "1,2,1",
            "1,2,2",
            "1,3,1",
            "0,4,1",
            "3,1,100")
    fun execute(expectedCount: Int, pageNumber: Int, pageSize: Int) {
        val selectQuery =
                JdbcSelectQuery(
                        TemplatedSelectQuery(
                                FakeSelectQuery(),
                                mapOf("uri" to DbUri("table/hr.persons?page=$pageNumber,$pageSize")),
                                DbConnection(paginationQueryTemplate = "${'$'}{query} offset ${'$'}{uri.pageSize()*(uri.pageNumber()-1)} limit ${'$'}{uri.pageSize()}")),
                        this.dataSource!!)
        assertEquals(expectedCount, selectQuery.execute(hashMapOf()).count())
    }
}