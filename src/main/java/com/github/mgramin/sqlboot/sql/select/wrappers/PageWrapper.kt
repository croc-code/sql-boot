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

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.sql.select.SelectQuery
import java.util.stream.Stream

class PageWrapper
/**
 * Ctor.
 *
 * @param origin
 * @param pageNumber
 * @param pageSize
 */
(private val origin: SelectQuery,
 private val pageNumber: Int,
 private val pageSize: Int
) : SelectQuery {

    val sql = """select *
                |  from (${origin.query})""".trimMargin()

    @Throws(BootException::class)
    override fun select(): Stream<Map<String, Any>>? {
        val sqlQuery = origin.query
        println(sql)
        return null
    }

    override fun columns(): Map<String, String>? {
        return origin.columns()
    }

    @Throws(BootException::class)
    override fun dbHealth() {
        origin.dbHealth()
    }

    override fun getQuery(): String {
        return sql
    }

}
