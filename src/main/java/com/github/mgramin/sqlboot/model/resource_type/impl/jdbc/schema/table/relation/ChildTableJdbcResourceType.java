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

package com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.table.relation;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource.impl.DbResourceImpl;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.table.TableJdbcResourceType;
import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class ChildTableJdbcResourceType implements ResourceType {

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
    public ChildTableJdbcResourceType(final DataSource dataSource) {
        properties = new LinkedHashMap<>();
        properties.put("PKTABLE_CAT", "primary key table catalog being imported (may be null)");
        properties.put("PKTABLE_SCHEM", "primary key table schema being imported (may be null)");
        properties.put("PKTABLE_NAME", "primary key table name being imported");
        properties.put("PKCOLUMN_NAME", "primary key column name being imported");
        properties.put("FKTABLE_CAT", "foreign key table catalog (may be null)");
        properties.put("FKTABLE_SCHEM", "foreign key table schema (may be null)");
        properties.put("FKTABLE_NAME", "foreign key table name");
        properties.put("FKCOLUMN_NAME", "foreign key column name");
        properties.put("KEY_SEQ", "sequence number within a foreign key( a value of 1 represents the first column of the foreign key, a value of 2 would represent the second column within the foreign key).");
        properties.put("UPDATE_RULE", "What happens to a foreign key when the primary key is updated:\n" +
                " - importedNoAction - do not allow update of primary key if it has been imported\n" +
                " - importedKeyCascade - change imported key to agree with primary key update\n" +
                " - importedKeySetNull - change imported key to NULL if its primary key has been updated\n" +
                " - importedKeySetDefault - change imported key to default values if its primary key has been updated\n" +
                " - importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)");
        properties.put("DELETE_RULE", "What happens to the foreign key when primary is deleted.\n" +
                " - importedKeyNoAction - do not allow delete of primary key if it has been imported\n" +
                " - importedKeyCascade - delete rows that import a deleted key\n" +
                " - importedKeySetNull - change imported key to NULL if its primary key has been deleted\n" +
                " - importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)\n" +
                " - importedKeySetDefault - change imported key to default if its primary key has been deleted");
        properties.put("FK_NAME", "foreign key name (may be null)");
        properties.put("PK_NAME", "primary key name (may be null)");
        properties.put("DEFERRABILITY", "can the evaluation of foreign key constraints be deferred until commit\n" +
                " - importedKeyInitiallyDeferred - see SQL92 for definition\n" +
                " - importedKeyInitiallyImmediate - see SQL92 for definition\n" +
                " - importedKeyNotDeferrable - see SQL92 for definition");

        this.dataSource = dataSource;
    }

    @Override
    public String name() {
        return "child_table";
    }

    @Override
    public List<String> aliases() {
        return asList("child_table", "child", "chld");
    }

    @Override
    public List<String> path() {
        return asList("schema", "table");
    }

    @Override
    public Stream<DbResource> read(final Uri uri) throws BootException {
        try {
            final TableJdbcResourceType tableJdbcResourceType = new TableJdbcResourceType(dataSource);
            final Stream<DbResource> tables = tableJdbcResourceType.read(uri);

            final List<DbResource> result = new ArrayList<>();

            try (final Connection connection = dataSource.getConnection()) {
                for (final DbResource table : tables.collect(toList())) {
                    final String tableSchem = table.headers().get("TABLE_SCHEM").toString();
                    final String tableName = table.headers().get("TABLE_NAME").toString();

                    final ResultSet parentTables = connection.getMetaData()
                            .getExportedKeys(null, tableSchem, tableName);

                    final ResultSetMetaData tableMetaData = parentTables.getMetaData();
                    final int columnsCount = tableMetaData.getColumnCount();
                    while (parentTables.next()) {
                        final String pkName = columnsCount >= 13 ? parentTables.getString(13) : null;

                        final Map<String, Object> props = new LinkedHashMap<>();
                        int i = 1;
                        for (String s : properties.keySet()) {
                            props.put(s, columnsCount >= i ? parentTables.getString(i++) : null);
                        }

                        result.add(new DbResourceImpl(pkName, this, new DbUri(name(),
                                asList(tableSchem, tableName, pkName)), props));
                    }
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
