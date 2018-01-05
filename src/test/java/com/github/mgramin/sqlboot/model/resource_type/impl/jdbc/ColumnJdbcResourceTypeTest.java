/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Maksim Gramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.model.resource_type.impl.jdbc;

import java.util.List;
import java.util.stream.Stream;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import com.github.mgramin.sqlboot.model.uri.wrappers.SqlPlaceholdersWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class ColumnJdbcResourceTypeTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void name() {
    }

    @Test
    public void aliases() {
    }

    @Test
    public void read() {
        final ResourceType table = new ColumnJdbcResourceType(dataSource);
        final Stream<DbResource> tables = table.read(
                new SqlPlaceholdersWrapper(
                        new DbUri("table", asList("MAIN_SCHEMA", "CITY"))));
        tables.forEach(v -> System.out.println(v.name()));
    }

    @Test
    public void medataData() {
    }

    @Test
    public void path() {
        final ResourceType column = new ColumnJdbcResourceType(dataSource);
        assertEquals(3, column.path().size());
        assertEquals("schema", column.path().get(0));
        assertEquals("table", column.path().get(1));
        assertEquals("column", column.path().get(2));
    }

}