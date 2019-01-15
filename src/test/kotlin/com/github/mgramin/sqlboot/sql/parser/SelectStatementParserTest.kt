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

package com.github.mgramin.sqlboot.sql.parser

import com.github.mgramin.sqlboot.sql.select.impl.parser.SelectStatementParser
import org.junit.Assert.assertEquals
import org.junit.Test

class SelectStatementParserTest {

    @Test
    fun parse() {
        val selectStatement = SelectStatementParser("""
            select "name"    as "persons_name"   /* name of person {key:value, key2:value2} */
                 , p.age     as persons_age      /* age of person */
                 , address   as "address"        /* address of person */
                 , test_column
                 , to_char(s.logon_time,'dd.mm.yyyy hh24:mi:ss') as logon_time /* logon_time comment */
              from persons p where 1=1""").parse()

        assertEquals("persons", selectStatement.tableName())

        val iterator = selectStatement.columns().iterator()

        val columnName = iterator.next()
        assertEquals("persons_name", columnName.name())
        assertEquals("name of person {key:value, key2:value2}", columnName.comment())

        val columnAge = iterator.next()
        assertEquals("persons_age", columnAge.name())
        assertEquals("age of person", columnAge.comment())

        val columnAddress = iterator.next()
        assertEquals("address", columnAddress.name())
        assertEquals("address of person", columnAddress.comment())

        val testColumn = iterator.next()
        assertEquals("test_column", testColumn.name())

        val toCharColumn = iterator.next()
        assertEquals("logon_time", toCharColumn.name())
    }
}