/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2017 Maksim Gramin
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

package com.github.mgramin.sqlboot.resource_types.impl;

import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.resource_types.impl.FsResourceTypes;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class FsResourceTypesTest {

    // TODO use parametrized tests ?

    @Test
    public void loadFromSql() throws Exception {
        final FsResourceTypes types = new FsResourceTypes(new FileSystemResource("conf/h2/database"));
        types.init();

        final ResourceType table = types.type("table");
        assertEquals(1, table.read(new DbUri(table.name(), "bookings", "airports")).size());

        final ResourceType tables = types.type("table");
        assertEquals(5, tables.read(new DbUri(tables.name(), "bookings")).size());
    }

    @Test
    public void loadFromJdbc() throws Exception {
        final FsResourceTypes types = new FsResourceTypes(new FileSystemResource("conf/common/database"));
        types.init();

        final ResourceType table = types.type("table");
        assertEquals(1, table.read(new DbUri(table.name(), "bookings", "airports")).size());

        final ResourceType tables = types.type("table");
        assertEquals(5, tables.read(new DbUri(tables.name(), "bookings")).size());
    }


    @Test
    public void findByName() throws Exception {
        FsResourceTypes types = new FsResourceTypes(new FileSystemResource("conf/h2/database"));
        types.init();
        assertEquals("schema", types.type("schema").name());
        assertEquals("table", types.type("table").name());
        assertEquals("column", types.type("column").name());
        assertEquals("pk", types.type("pk").name());
//        assertEquals("fk", types.type("fk").name());
//        assertEquals("index", types.type("index").name());
    }

}