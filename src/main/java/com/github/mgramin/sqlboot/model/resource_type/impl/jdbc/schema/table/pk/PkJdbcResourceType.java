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

package com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.table.pk;

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
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.table.TableJdbcResourceType;
import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class PkJdbcResourceType implements ResourceType {

    /**
     *
     */
    private final DataSource dataSource;

    /**
     *
     */
    private final Map<String, String> properties;

    /**
     * Ctor.
     *
     * @param dataSource
     */
    public PkJdbcResourceType(final DataSource dataSource) {
        properties = new LinkedHashMap<>();
        properties.put("TABLE_CAT", "table catalog (may be null)");
        properties.put("TABLE_SCHEM", "table schema (may be null)");
        properties.put("TABLE_NAME", "table name");
        properties.put("COLUMN_NAME", "column name");
        properties.put("KEY_SEQ", "sequence number within primary key( a value of 1 represents the first column of the primary key, a value of 2 would represent the second column within the primary key).");
        properties.put("PK_NAME", "primary key name (may be null)");
        this.dataSource = dataSource;
    }

    @Override
    public String name() {
        return "pk";
    }

    @Override
    public List<String> aliases() {
        return asList("primary_key", "pk");
    }

    @Override
    public List<String> path() {
        return asList("schema", "pk");
    }

    @Override
    public Stream<DbResource> read(final Uri uri) throws BootException {
        try {
            final TableJdbcResourceType tableJdbcResourceType = new TableJdbcResourceType(dataSource);
            final Stream<DbResource> tables = tableJdbcResourceType.read(uri);

            final List<DbResource> result = new ArrayList<>();

            for (final DbResource table : tables.collect(toList())) {
                final String tableSchem = table.headers().get("TABLE_SCHEM").toString();
                final String tableName = table.headers().get("TABLE_NAME").toString();

                final ResultSet primaryKeys = dataSource.getConnection().getMetaData()
                        .getPrimaryKeys(null, tableSchem, tableName);

                final ResultSetMetaData tableMetaData = primaryKeys.getMetaData();
                final int columnsCount = tableMetaData.getColumnCount();
                while (primaryKeys.next()) {
                    final String pkName = columnsCount >= 6 ? primaryKeys.getString(6) : null;

                    final Map<String, Object> props = new LinkedHashMap<>();
                    int i = 1;
                    for (String s : properties.keySet()) {
                        props.put(s, columnsCount >= i ? primaryKeys.getString(i++) : null);
                    }

                    result.add(new DbResourceImpl(pkName, this, new DbUri(name(),
                            asList(tableSchem, tableName, pkName)), props));
                }
            }
            return result.stream();
        } catch (SQLException e) {
            throw new BootException(e);
        }
    }

    @Override
    public Map<String, String> metaData() {
        return properties;
    }

}
