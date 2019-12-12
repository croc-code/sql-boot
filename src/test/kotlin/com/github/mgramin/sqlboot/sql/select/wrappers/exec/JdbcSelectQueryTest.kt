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

package com.github.mgramin.sqlboot.sql.select.wrappers.exec

import com.github.mgramin.sqlboot.sql.select.SelectQuery
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
        val rows = JdbcSelectQuery(FakeSelectQuery(), this.dataSource!!).execute(hashMapOf()).collectList().block()
        assertEquals(arrayListOf(
                linkedMapOf("n" to "mkyong", "mail" to "mkyong@gmail.com", "registration_date" to ""),
                linkedMapOf("n" to "alex", "mail" to "alex@yahoo.com", "registration_date" to ""),
                linkedMapOf("n" to "joel", "mail" to "joel@gmail.com", "registration_date" to "")),
                rows)
    }

    @Test
    @Deprecated("Move to base test class")
    fun columns() {
        val columns = JdbcSelectQuery(FakeSelectQuery(), this.dataSource!!).columns()
        assertEquals(listOf(
                SelectQuery.Column("n", "VARCHAR", "First name", emptyMap()),
                SelectQuery.Column("mail", "VARCHAR", "Personal email", emptyMap()),
                SelectQuery.Column("registration_date", "timestamptz", "Registration date", emptyMap())
        ), columns)
    }

    @Test
    @Deprecated("Move to base test class")
    fun query() {
        assertEquals(
                """select n                 /* First name */
                  |     , mail              /* Personal email */
                  |     , registration_date /* Registration date */
                  |  from (select name  as n
                  |             , email as mail
                  |             , registration_date
                  |          from main_schema.users)""".trimMargin(),
                JdbcSelectQuery(FakeSelectQuery(), dataSource!!).query())
    }

}