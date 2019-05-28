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

package com.github.mgramin.sqlboot.tools.files.file.impl

import org.junit.jupiter.api.Test
import java.io.IOException
import kotlin.test.assertEquals

class MarkdownFileTest {

    @Test
    @Throws(IOException::class)
    fun parse() {
        val text = """
            |````sql
            |select u.username     as "@schema"
            |     , u.user_id      as "user_id"
            |     , u.created      as "created"
            |  from all_users u
            | order by u.username
            |````
            |
            |### row_count
            |
            |````sql
            |select count(1)
            |  from all_users u
            | order by u.username
            |````
            |""".trimMargin()
        val map = MarkdownFile("test.md", text).parse()
//        assertEquals(arrayListOf("", "row_count"), map.keys.toList())
        assertEquals(map["1"], """select u.username     as "@schema"
                                |     , u.user_id      as "user_id"
                                |     , u.created      as "created"
                                |  from all_users u
                                | order by u.username""".trimMargin())

        assertEquals(map["2"], """select count(1)
                                         |  from all_users u
                                         | order by u.username""".trimMargin())
    }
}