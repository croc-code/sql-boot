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

package com.github.mgramin.sqlboot.model.resourcetype.impl.sql

import com.github.mgramin.sqlboot.model.resourcetype.wrappers.list.WhereWrapper
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.sql.select.impl.JdbcSelectQuery
import com.github.mgramin.sqlboot.template.generator.impl.FakeTemplateGenerator
import com.github.mgramin.sqlboot.template.generator.impl.GroovyTemplateGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.Arrays.asList
import javax.sql.DataSource

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 3d61942b737e9bf2768c93c6058e1ec55929b6f3 $
 * @since 0.1
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(locations = ["/test_config.xml"])
class SqlResourceTypeTest {

    @Autowired
    internal var dataSource: DataSource? = null

    @Test
    fun name() {
        // TODO use FakeSelectQuery
        val table = SqlResourceType(JdbcSelectQuery(dataSource!!, FakeTemplateGenerator("sql query ...")), arrayListOf("table", "tbl", "t"))
        assertEquals("table", table.name())
    }

    @Test
    fun aliases() {
        val table = SqlResourceType(JdbcSelectQuery(dataSource!!, FakeTemplateGenerator("sql query ...")), arrayListOf("table", "tbl", "t"))
        assertEquals(arrayListOf("table", "tbl", "t"), table.aliases())
    }

    @Test
    fun read() {
        val sql = """select *
                    |  from (select table_schema   as "@schema"
                    |             , table_name     as "@table"
                    |          from information_schema.tables)""".trimMargin()
        val type = WhereWrapper(SqlResourceType(JdbcSelectQuery(dataSource!!, GroovyTemplateGenerator(sql)), arrayListOf("table", "tbl", "t")))
        assertEquals(4, type.read(DbUri("table/m.column")).count())
    }

    @Test
    fun read2() {
        val sql = """select *
                    |  from (select table_schema    as "@schema"
                    |             , table_name      as "@table"
                    |             , column_name     as "@column"
                    |          from information_schema.columns)""".trimMargin()
        val type = WhereWrapper(
                SqlResourceType(JdbcSelectQuery(dataSource!!, GroovyTemplateGenerator(sql)), asList("column")))
        assertEquals(8, type.read(DbUri("column/main_schema.users")).count())
    }

    @Test
    fun path() {
        val sql = """select @schema
                    |     , @table
                    |     , @column
                    |  from (select table_schema    as "@schema"
                    |             , table_name      as "@table"
                    |             , column_name     as "@column"
                    |          from information_schema.columns)""".trimMargin()
        val type = SqlResourceType(JdbcSelectQuery(dataSource!!, GroovyTemplateGenerator(sql)), asList("column"))
        assertEquals("[schema, table, column]", type.path().toString())
    }

    @Test
    fun metaData() {
    }
}