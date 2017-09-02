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

package com.github.mgramin.sqlboot.model.resource_type.impl.sql;

import java.util.List;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.resource_type.wrappers.list.WhereWrapper;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import com.github.mgramin.sqlboot.sql.impl.JdbcSqlQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.junit.Assert.assertEquals;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class SqlResourceTypeTest {

    @Autowired
    DataSource dataSource;

    @Test
    public void name() throws Exception {
        final SqlResourceType table = new SqlResourceType(new JdbcSqlQuery(dataSource),
            asList("table"), "sql query ...");
        assertEquals("table", table.name());
    }

    @Test
    public void aliases() throws Exception {
        final SqlResourceType table = new SqlResourceType(new JdbcSqlQuery(dataSource),
            asList("table"), "sql query ...");
        assertEquals("[table]", table.aliases().toString());
    }

    @Test
    public void read() throws Exception {
        final String sql = "select * from (select table_schema as \"@table_schema\", table_name as \"@table_name\" "
                + "from information_schema.tables)";
        final ResourceType type = new WhereWrapper(
                new SqlResourceType(new JdbcSqlQuery(dataSource), asList("table"), sql));
        List<DbResource> resources = type.read(new DbUri("table/m.column"));
        assertEquals(3, resources.size());
    }

    @Test
    public void read2() throws Exception {
        final String sql = "select * from (select table_schema as \"@table_schema\", table_name as \"@table_name\", column_name as \"@column_name\""
                        + "from information_schema.columns)";
        ResourceType type = new WhereWrapper(
                new SqlResourceType(new JdbcSqlQuery(dataSource), asList("column"), sql));
        List<DbResource> resources = type.read(
                new DbUri("column/main_schema.users"));
        assertEquals(4, resources.size());
    }

}