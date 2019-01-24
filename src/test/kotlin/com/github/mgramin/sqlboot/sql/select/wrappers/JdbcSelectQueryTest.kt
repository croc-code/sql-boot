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

import com.github.mgramin.sqlboot.sql.select.impl.FakeSelectQuery
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.sql.DataSource

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 550c861e4b0f97eb68e0b0d6bb0d0b99e2af1e83 $
 * @since 0.1
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(locations = ["/test_config.xml"])
class JdbcSelectQueryTest {

    @Autowired
    internal var dataSource: DataSource? = null

    @Test
    fun execute() {
        val rows = JdbcSelectQuery(FakeSelectQuery(), this.dataSource!!).execute(hashMapOf()).toList()
        assertEquals(arrayListOf(linkedMapOf("n" to "mkyong", "mail" to "mkyong@gmail.com"),
                linkedMapOf("n" to "alex", "mail" to "alex@yahoo.com"),
                linkedMapOf("n" to "joel", "mail" to "joel@gmail.com")),
                rows)
    }

    @Test
    @Deprecated("Move to base test class")
    fun columns() {
        val columns = JdbcSelectQuery(FakeSelectQuery(), this.dataSource!!).columns()
        assertEquals(mapOf("n" to "First name", "mail" to "Personal email"), columns)
    }

    @Test
    @Deprecated("Move to base test class")
    fun query() {
        assertEquals(
                """select "n"        /* First name */
                  |     , "mail"     /* Personal email */
                  |  from (select name  as "n"
                  |             , email as "mail"
                  |          from main_schema.users)""".trimMargin(),
                JdbcSelectQuery(FakeSelectQuery(), dataSource!!).query())
    }

}