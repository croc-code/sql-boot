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

package com.github.mgramin.sqlboot.sql.select.impl

import com.github.mgramin.sqlboot.sql.select.SelectQuery
import com.github.mgramin.sqlboot.template.generator.impl.FakeTemplateGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class SimpleSelectQueryTest {

    @Test
    fun query() {
        val template = "select name /* First name*/ from persons"
        assertEquals(template,
                SimpleSelectQuery(FakeTemplateGenerator(template)).query())
    }

    @Test
    fun properties() {
        val template = """/* { "dialect": "h2" } */
                         |select name /* First name*/
                         |  from persons""".trimMargin()
        assertEquals(mapOf("dialect" to "h2"), SimpleSelectQuery(FakeTemplateGenerator(template)).properties())
    }

    @Test
    fun emptyProperties() {
        val template = """select name /* First name*/
                         |  from persons""".trimMargin()
        assertEquals(emptyMap<String, String>(), SimpleSelectQuery(FakeTemplateGenerator(template)).properties())
    }

    @Test
    fun columns() {
        assertEquals(listOf(SelectQuery.Column("name", "First name")),
                SimpleSelectQuery(FakeTemplateGenerator("select name /* First name */ from persons")).columns())
    }

    @Test
    fun execute() {
        assertThrows(RuntimeException::class.java) {
            SimpleSelectQuery(FakeTemplateGenerator("select name from persons")).execute(hashMapOf())
        }
    }

//    @Test
//    fun parse() {
//        val selectStatement = SelectStatementParser("""
//            select "name"    as "persons_name"   /* name of person {key:value, key2:value2} */
//                 , p.age     as persons_age      /* age of person */
//                 , address   as "address"        /* address of person */
//                 , test_column
//                 , to_char(s.logon_time,'dd.mm.yyyy hh24:mi:ss') as logon_time /* logon_time properties */
//              from persons p where 1=1""").parse()
//
//        assertEquals("persons", selectStatement.tableName())
//
//        val iterator = selectStatement.columns().iterator()
//
//        val columnName = iterator.next()
//        assertEquals("persons_name", columnName.name())
//        assertEquals("name of person {key:value, key2:value2}", columnName.properties())
//
//        val columnAge = iterator.next()
//        assertEquals("persons_age", columnAge.name())
//        assertEquals("age of person", columnAge.properties())
//
//        val columnAddress = iterator.next()
//        assertEquals("address", columnAddress.name())
//        assertEquals("address of person", columnAddress.properties())
//
//        val testColumn = iterator.next()
//        assertEquals("test_column", testColumn.name())
//
//        val toCharColumn = iterator.next()
//        assertEquals("logon_time", toCharColumn.name())
//    }

}