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

package com.github.mgramin.sqlboot.model.resource_type.wrappers.header;

import static org.junit.Assert.assertEquals;

import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.FakeDbResourceType;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

public class SelectWrapperTest {

    final ResourceType fakeType = new SelectWrapper(new FakeDbResourceType());

    @Test
    public void name() throws Exception {
        assertEquals("fake_resource_type", fakeType.name());
    }

    @Test
    public void aliases() throws Exception {
        assertEquals("[fake_resource_type, fake_type, frt, f]",
            fakeType.aliases().toString());
    }

    @Test
    public void read() throws Exception {
        final List<DbResource> resources = fakeType.read(new DbUri("table/hr.persons?select=schema")).collect(
            Collectors.toList());
        for (DbResource resource : resources) {
            assertEquals(1, resource.headers().size());
        }
        assertEquals(3, resources.size());
    }

}