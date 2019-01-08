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
import com.github.mgramin.sqlboot.model.uri.impl.FakeUri
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.assertAll
import java.util.stream.Stream
import java.util.stream.Stream.of


class CacheWrapperTest {

    private val originType = mock<ResourceType> {
        on { aliases() } doReturn arrayListOf("fake_resource_type", "fake_type", "frt", "f")
        on { read(any()) } doReturn of(mock {}, mock {}, mock {})
    }
    private val cachedType = CacheWrapper(originType)

    @Test
    fun name() {
        assertEquals("fake_resource_type", cachedType.name())
    }

    @Test
    fun aliases() {
        assertEquals(arrayListOf("fake_resource_type", "fake_type", "frt", "f"), cachedType.aliases())
    }

    @Test
    fun read() {
        assertAll("Should return same objects",
                { assertEquals(3, cachedType.read(FakeUri()).count()) },
                { assertEquals(3, cachedType.read(FakeUri()).count()) },
                { assertEquals(3, cachedType.read(FakeUri()).count()) }
        )
        verify(originType, times(1)).read(any())
    }

}