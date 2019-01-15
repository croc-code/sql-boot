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

package com.github.mgramin.sqlboot.model.resourcetype.wrappers.list

import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.resourcetype.impl.FakeDbResourceType
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 5f0ba45b60cf370291901af45187999fed71f35e $
 * @since 0.1
 */
class LimitWrapperTest {

    private val type: ResourceType = LimitWrapper(FakeDbResourceType())

    @Test
    fun name() {
        assertEquals("fake_resource_type", type.name())
    }

    @Test
    fun aliases() {
        assertEquals("[fake_resource_type, fake_type, frt, f]",
                type.aliases().toString())
    }

    @Test
    fun read() {
        assertEquals(3, type.read(DbUri("table/hr.persons")).count())
        assertEquals(3, type.read(DbUri("table/hr.persons?limit=3")).count())
        assertEquals(2, type.read(DbUri("table/hr.persons?limit=2")).count())
        assertEquals(1, type.read(DbUri("table/hr.persons?limit=1")).count())
    }
}