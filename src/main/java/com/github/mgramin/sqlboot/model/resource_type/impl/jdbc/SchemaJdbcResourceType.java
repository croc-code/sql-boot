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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource.impl.DbResourceImpl;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import static java.util.Arrays.asList;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class SchemaJdbcResourceType implements ResourceType {

    /**
     *
     */
    private final DataSource dataSource;

    /**
     *
     * @param dataSource
     */
    public SchemaJdbcResourceType(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String name() {
        return "schema";
    }

    @Override
    public List<String> aliases() {
        return asList("schema", "schm", "s");
    }

    @Override
    public List<String> path() {
        return asList("schema");
    }

    @Override
    public Stream<DbResource> read(final Uri uri) throws BootException {
         try {
            final List<DbResource> result = new ArrayList<>();
            final ResultSet columns = dataSource.getConnection().getMetaData()
                    .getSchemas(null, uri.path(0));

            final ResultSetMetaData tableMetaData = columns.getMetaData();
            final int columnsCount = tableMetaData.getColumnCount();
            while (columns.next()) {
                final String schemaName = columnsCount >= 1 ? columns.getString(1) : null;

                final Map<String, Object> properties = new LinkedHashMap<>();

                properties.put("TABLE_SCHEM", columnsCount >= 1 ? columns.getString(1) : null);
                properties.put("TABLE_CATALOG", columnsCount >= 2 ? columns.getString(2) : null);

                result.add(new DbResourceImpl(schemaName, this,
                                new DbUri(name(), asList(schemaName)),
                                properties));
            }
            return result.stream();
        } catch (SQLException e) {
            throw new BootException(e);
        }
    }

    @Override
    public Map<String, String> metaData() {
         final Map<String, String> properties = new LinkedHashMap<>();

        properties.put("TABLE_SCHEM", "table schema (may be null)");
        properties.put("TABLE_CATALOG", "table catalog (may be null)");

        return properties;
    }

}
