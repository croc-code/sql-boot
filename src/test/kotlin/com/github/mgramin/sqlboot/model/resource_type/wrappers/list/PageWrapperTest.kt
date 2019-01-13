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

package com.github.mgramin.sqlboot.model.resource_type.wrappers.list

import com.github.mgramin.sqlboot.model.resource_type.ResourceType
import com.github.mgramin.sqlboot.model.resource_type.impl.FakeDbResourceType
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class PageWrapperTest {

    private val type: ResourceType = PageWrapper(FakeDbResourceType())

    @Test
    fun name() {
    }

    @Test
    fun aliases() {
    }

    @Test
    fun path() {
    }

    @Test
    fun metaData() {
    }

    @ParameterizedTest
    @CsvSource("persons;**/table/hr?page=1,1",
            "users;**/table/hr?page=2,1",
            "jobs;**/table/hr?page=3,1", delimiter = ';')
    fun read(name: String, uri: String) {
        assertEquals(name, type.read(DbUri(uri)).findAny().get().name())
    }

    @ParameterizedTest
    @CsvSource("1;table/hr?page=1,1",
            "2;table/hr?page=1,2",
            "3;table/hr?page=1,3",
            "3;table/hr?page=1",
            "0;table/hr?page=2", delimiter = ';')
    fun read2(count: Long, uri: String) {
        assertEquals(count, type.read(DbUri(uri)).count())
    }
}