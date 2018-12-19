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

package com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.table;

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
     *
     */
    private final Map<String, String> properties;

    /**
     * Ctor.
     *
     * @param dataSource
     */
    public TableJdbcResourceType(final DataSource dataSource) {
        properties = new LinkedHashMap<>();
        properties.put("TABLE_CAT", "table catalog (may be null)");
        properties.put("TABLE_SCHEM", "table schema (may be null)");
        properties.put("TABLE_NAME", "table name");
        properties.put("TABLE_TYPE", "table type. Typical types are \"TABLE\", \"VIEW\", \"SYSTEM TABLE\", \"GLOBAL TEMPORARY\", \"LOCAL TEMPORARY\", \"ALIAS\", \"SYNONYM\".");
        properties.put("REMARKS", "explanatory comment on the table");
        properties.put("TYPE_CAT", "the types catalog (may be null)");
        properties.put("TYPE_SCHEM", "the types schema (may be null)");
        properties.put("TYPE_NAME", "type name (may be null)");
        properties.put("SELF_REFERENCING_COL_NAME", "name of the designated \"identifier\" column of a typed table (may be null)");
        properties.put("REF_GENERATION", "specifies how values in SELF_REFERENCING_COL_NAME are created. Values are \"SYSTEM\", \"USER\", \"DERIVED\". (may be null)");
        this.dataSource = dataSource;
    }

    @Override
    public List<String> aliases() {
        return asList("table", "tbl", "t");
    }

    @Override
    public List<String> path() {
        return asList("schema", "table");
    }

    @Override
    public Stream<DbResource> read(final Uri uri) throws BootException {
        try {
            final List<DbResource> result = new ArrayList<>();
            final ResultSet tables = dataSource.getConnection().getMetaData()
                    .getTables(null, uri.path(0), uri.path(1), new String[]{"TABLE"});

            final ResultSetMetaData tableMetaData = tables.getMetaData();
            final int columnsCount = tableMetaData.getColumnCount();
            while (tables.next()) {
                final String tableSchem = columnsCount >= 2 ? tables.getString(2) : null;
                final String tableName = columnsCount >= 3 ? tables.getString(3) : null;

                final Map<String, Object> props = new LinkedHashMap<>();
                int i = 1;
                for (String s : properties.keySet()) {
                    props.put(s, columnsCount >= i ? tables.getString(i++) : null);
                }

                result.add(new DbResourceImpl(tableName, this, new DbUri(name(),
                        asList(tableSchem, tableName)), props));
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
