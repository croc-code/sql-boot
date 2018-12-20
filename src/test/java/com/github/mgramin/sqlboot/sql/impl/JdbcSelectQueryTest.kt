/**
 * The MIT License (MIT)
 *
 *
 * Copyright (c) 2016-2017 Maksim Gramin
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.sql.impl

import java.util.stream.Collectors.toList
import org.junit.Assert.assertEquals

import com.github.mgramin.sqlboot.sql.select.SelectQuery
import com.github.mgramin.sqlboot.sql.select.impl.JdbcSelectQuery
import com.github.mgramin.sqlboot.template.generator.impl.GroovyTemplateGenerator
import javax.sql.DataSource
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import java.util.stream.Collectors

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 550c861e4b0f97eb68e0b0d6bb0d0b99e2af1e83 $
 * @since 0.1
 */
@RunWith(SpringRunner::class)
@ContextConfiguration(locations = arrayOf("/test_config.xml"))
class JdbcSelectQueryTest {

    @Autowired
    internal var dataSource: DataSource? = null

    @Test
    fun select() {
        val select = JdbcSelectQuery(dataSource,
                "select * from (select name AS \"n\", email as \"mail\" from main_schema.users)")
                .select().collect(Collectors.toList())
        assertEquals(select.toString(),
                "[{n=mkyong, mail=mkyong@gmail.com}, {n=alex, mail=alex@yahoo.com}, {n=joel, mail=joel@gmail.com}]")
    }

    @Test
    @Throws(Exception::class)
    fun medataData() {
        val selectQuery = JdbcSelectQuery(dataSource,
                GroovyTemplateGenerator("select name as name /* name */, email as email /* email */ from main_schema.users"))
        val metadata = selectQuery.columns()
        println(metadata)
        assertEquals("email", metadata["email"])
        assertEquals("name", metadata["name"])
    }

    @Test
    @Throws(Exception::class)
    fun dbHealth() {
        JdbcSelectQuery(dataSource, "").dbHealth()
    }

}