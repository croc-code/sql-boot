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
public class TableJdbcResourceType implements ResourceType {

    /**
     *
     */
    private final DataSource dataSource;

    /**
     * Ctor.
     *
     * @param dataSource
     */
    public TableJdbcResourceType(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String name() {
        return "table";
    }

    @Override
    public List<String> aliases() {
        return asList("table", "tbl", "t");
    }

    @Override
    public Stream<DbResource> read(final Uri uri) throws BootException {
        final List<DbResource> result = new ArrayList<>();
        try {
            final ResultSet tables = dataSource.getConnection().getMetaData()
                    .getTables(null, uri.path(0), uri.path(1), new String[]{"TABLE"});

            final ResultSetMetaData tableMetaData = tables.getMetaData();
            final int columnsCount = tableMetaData.getColumnCount();
            while (tables.next()) {
                final String tableSchem = columnsCount >= 2 ? tables.getString(2) : null;
                final String tableName = columnsCount >= 3 ? tables.getString(3) : null;

                final Map<String, Object> properties = new LinkedHashMap<>();

                properties.put("TABLE_CAT", columnsCount >= 1 ? tables.getString(1) : null);
                properties.put("TABLE_SCHEM", columnsCount >= 2 ? tables.getString(2) : null);
                properties.put("TABLE_NAME", columnsCount >= 3 ? tables.getString(3) : null);
                properties.put("TABLE_TYPE", columnsCount >= 4 ? tables.getString(4) : null);
                properties.put("REMARKS", columnsCount >= 5 ? tables.getString(5) : null);
                properties.put("TYPE_CAT", columnsCount >= 6 ? tables.getString(6) : null);
                properties.put("TYPE_SCHEM", columnsCount >= 7 ? tables.getString(7) : null);
                properties.put("TYPE_NAME", columnsCount >= 8 ? tables.getString(8) : null);
                properties.put("SELF_REFERENCING_COL_NAME", columnsCount >= 9 ? tables.getString(9) : null);
                properties.put("REF_GENERATION", columnsCount >= 10 ? tables.getString(10) : null);

                result.add(new DbResourceImpl(tableName, this,
                                new DbUri(name(), asList(tableSchem, tableName)),
                                properties));
            }
        } catch (SQLException e) {
            throw new BootException(e);
        }
        return result.stream();
    }

    @Override
    public Map<String, String> medataData() {
        return null;
    }

}
