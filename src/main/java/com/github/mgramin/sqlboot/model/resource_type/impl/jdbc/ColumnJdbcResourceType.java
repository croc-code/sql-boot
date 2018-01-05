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
public class ColumnJdbcResourceType implements ResourceType {
     
    /**
     *
     */
    private final DataSource dataSource;

    /**
     * 
     * @param dataSource
     */
    public ColumnJdbcResourceType(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String name() {
        return "column";
    }

    @Override
    public List<String> aliases() {
        return asList("column", "clmn", "c");
    }

    @Override
    public Stream<DbResource> read(final Uri uri) throws BootException {
        try {
            final List<DbResource> result = new ArrayList<>();
            final ResultSet columns = dataSource.getConnection().getMetaData()
                    .getColumns(null, uri.path(0), uri.path(1), uri.path(2));
            
            final ResultSetMetaData tableMetaData = columns.getMetaData();
            final int columnsCount = tableMetaData.getColumnCount();
            while (columns.next()) {
                final String schemaName = columnsCount >= 2 ? columns.getString(2) : null;
                final String tableName = columnsCount >= 3 ? columns.getString(3) : null;
                final String columnName = columnsCount >= 4 ? columns.getString(4) : null;

                final Map<String, Object> properties = new LinkedHashMap<>();

                properties.put("TABLE_CAT", columnsCount >= 1 ? columns.getString(1) : null);
                properties.put("TABLE_SCHEM", columnsCount >= 2 ? columns.getString(2) : null);
                properties.put("TABLE_NAME", columnsCount >= 3 ? columns.getString(3) : null);
                properties.put("COLUMN_NAME", columnsCount >= 4 ? columns.getString(4) : null);
                properties.put("DATA_TYPE", columnsCount >= 5 ? columns.getString(5) : null);

                /*properties.put("TYPE_CAT", columnsCount >= 6 ? columns.getString(6) : null);
                properties.put("TYPE_SCHEM", columnsCount >= 7 ? columns.getString(7) : null);
                properties.put("TYPE_NAME", columnsCount >= 8 ? columns.getString(8) : null);
                properties.put("SELF_REFERENCING_COL_NAME", columnsCount >= 9 ? columns.getString(9) : null);
                properties.put("REF_GENERATION", columnsCount >= 10 ? columns.getString(10) : null);*/

                result.add(new DbResourceImpl(columnName, this,
                                new DbUri(name(), asList(schemaName, tableName, columnName)),
                                properties));
            }
            return result.stream();           
        } catch (SQLException e) {
            throw new BootException(e);
        }
    }

    @Override
    public Map<String, String> metaData() {
        return null;
    }
}
