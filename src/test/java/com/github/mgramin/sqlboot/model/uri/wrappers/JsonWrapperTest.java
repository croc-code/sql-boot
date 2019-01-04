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

package com.github.mgramin.sqlboot.model.uri.wrappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import org.junit.Test;

public class JsonWrapperTest {

    final Uri uri = new JsonWrapper(
        new DbUri("table/hr.*persons*/"));

    @Test
    public void type() throws Exception {
        assertEquals("table", uri.type());
    }

    @Test
    public void path() throws Exception {
        assertEquals("%persons%", uri.path().get(1));
    }

    @Test
    public void recursive() throws Exception {
        assertTrue(uri.recursive());
    }

    @Test
    public void params() throws Exception {
        assertEquals(0, uri.params().size());
    }

    @Test
    public void toStringTest() throws Exception {
        assertEquals("DbUri{type='table', path=[hr, *persons*], recursive=true, params={}}",
            uri.toString());
    }

}