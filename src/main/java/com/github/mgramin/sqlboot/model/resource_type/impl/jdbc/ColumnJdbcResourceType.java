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
     */
    private final Map<String, String> properties;

    /**
     * 
     * @param dataSource
     */
    public ColumnJdbcResourceType(DataSource dataSource) {
        properties = new LinkedHashMap<>();
        properties.put("TABLE_CAT", "table catalog (may be null)");
        properties.put("TABLE_SCHEM", "table schema (may be null)");
        properties.put("TABLE_NAME", "table name");
        properties.put("COLUMN_NAME", "column name");
        properties.put("DATA_TYPE", "SQL type from java.sql.Types");
        properties.put("TYPE_NAME", "Data source dependent type name, for a UDT the type name is fully qualified");
        properties.put("COLUMN_SIZE", "column size.");
        properties.put("BUFFER_LENGTH", "is not used.");
        properties.put("DECIMAL_DIGITS", "the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.");
        properties.put("NUM_PREC_RADIX", "Radix (typically either 10 or 2)");
        properties.put("NULLABLE", "is NULL allowed. columnNoNulls - might not allow NULL values; columnNullable - definitely allows NULL values; columnNullableUnknown - nullability unknown");
        properties.put("REMARKS", "comment describing column (may be null)");
        properties.put("COLUMN_DEF", "default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)");
        properties.put("SQL_DATA_TYPE", "unused");
        properties.put("SQL_DATETIME_SUB", "unused");
        properties.put("CHAR_OCTET_LENGTH", "for char types the maximum number of bytes in the column");
        properties.put("ORDINAL_POSITION", "index of column in table (starting at 1)");
        properties.put("IS_NULLABLE", "ISO rules are used to determine the nullability for a column. YES --- if the column can include NULLs; NO --- if the column cannot include NULLs; empty string --- if the nullability for the column is unknown");
        properties.put("SCOPE_CATALOG", "catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)");
        properties.put("SCOPE_SCHEMA", "schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)");
        properties.put("SCOPE_TABLE", "table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF)");
        properties.put("SOURCE_DATA_TYPE", "source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)");
        properties.put("IS_AUTOINCREMENT", "Indicates whether this column is auto incremented. YES --- if the column is auto incremented; NO --- if the column is not auto incremented; empty string --- if it cannot be determined whether the column is auto incremented");
        properties.put("IS_GENERATEDCOLUMN", "Indicates whether this is a generated column. YES --- if this a generated column; NO --- if this not a generated column; empty string --- if it cannot be determined whether this is a generated column");

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
    public List<String> path() {
        return asList("schema", "table", "column");
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

                final Map<String, Object> props = new LinkedHashMap<>();
                int i = 1;
                for (String s : properties.keySet()) {
                    props.put(s, columnsCount >= i ? columns.getString(i++) : null);
                }

                result.add(new DbResourceImpl(columnName, this, new DbUri(name(),
                        asList(schemaName, tableName, columnName)), props));
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
