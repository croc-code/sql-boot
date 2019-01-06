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

package com.github.mgramin.sqlboot.model.resource_type.wrappers.header

import com.github.mgramin.sqlboot.model.resource_type.ResourceType
import com.github.mgramin.sqlboot.model.resource_type.impl.FakeDbResourceType
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import org.junit.Assert.assertEquals
import org.junit.Test

class SelectWrapperTest {

    private val fakeType: ResourceType = SelectWrapper(FakeDbResourceType())

    @Test
    @Throws(Exception::class)
    fun name() {
        assertEquals("fake_resource_type", fakeType.name())
    }

    @Test
    @Throws(Exception::class)
    fun aliases() {
        assertEquals("[fake_resource_type, fake_type, frt, f]",
                fakeType.aliases().toString())
    }

    @Test
    @Throws(Exception::class)
    fun read() {
        val resources = fakeType.read(DbUri("table/hr.persons?select=schema")).iterator().asSequence().toList()
        for (resource in resources) {
            assertEquals(1, resource.headers().size.toLong())
        }
        assertEquals(3, resources.size)
    }

}