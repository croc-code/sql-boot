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

package com.github.mgramin.sqlboot.model.uri.wrappers

import com.github.mgramin.sqlboot.model.uri.Uri
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class JsonWrapperTest {

    private val uri: Uri = JsonWrapper(DbUri("table/hr.*persons*/?select=name,age"))

    @Test
    fun type() {
        assertEquals("table", uri.type())
    }

    @Test
    fun path() {
        assertEquals(arrayListOf("hr", "%persons%"), uri.path())
    }

    @Test
    fun pathIndex() {
        assertEquals("%persons%", uri.path(1))
    }

    @Test
    fun recursive() {
        assertTrue(uri.recursive()!!)
    }

    @Test
    fun params() {
        assertEquals(1, uri.params().size)
    }

    @Test
    fun toStringTest() {
        assertEquals("DbUri{type='table', path=[hr, *persons*], recursive=true, params={select=name,age}}",
                uri.toString())
    }
}