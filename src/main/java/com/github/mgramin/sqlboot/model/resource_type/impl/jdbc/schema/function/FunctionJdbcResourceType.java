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

package com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.function;

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
public class FunctionJdbcResourceType implements ResourceType {

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
    public FunctionJdbcResourceType(final DataSource dataSource) {
        properties = new LinkedHashMap<>();
        properties.put("FUNCTION_CAT", "function catalog (may be null)");
        properties.put("FUNCTION_SCHEM", "function schema (may be null)");
        properties.put("FUNCTION_NAME", "function name. This is the name used to invoke the function");
        properties.put("REMARKS", "explanatory comment on the function");
        properties.put("FUNCTION_TYPE", "kind of function:\n" +
                "  - functionResultUnknown - Cannot determine if a return value or table will be returned\n" +
                "  - functionNoTable- Does not return a table\n" +
                "  - functionReturnsTable - Returns a table");
        properties.put("SPECIFIC_NAME", "the name which uniquely identifies this function within its schema. This is a user specified, or DBMS generated, name that may be different then the FUNCTION_NAME for example with overload functions");

        this.dataSource = dataSource;
    }

    @Override
    public String name() {
        return "func";
    }

    @Override
    public List<String> aliases() {
        return asList("function", "func", "fnc", "f");
    }

    @Override
    public List<String> path() {
        return asList("schema", "func");
    }

    @Override
    public Stream<DbResource> read(final Uri uri) throws BootException {
        try {
            final List<DbResource> result = new ArrayList<>();
            final ResultSet tables = dataSource.getConnection().getMetaData()
                    .getFunctions(null, uri.path(0), uri.path(1));

            final ResultSetMetaData tableMetaData = tables.getMetaData();
            final int columnsCount = tableMetaData.getColumnCount();
            while (tables.next()) {
                final String tableSchem = columnsCount >= 2 ? tables.getString(2) : null;
                final String functionName = columnsCount >= 3 ? tables.getString(3) : null;

                final Map<String, Object> props = new LinkedHashMap<>();
                int i = 1;
                for (String s : properties.keySet()) {
                    props.put(s, columnsCount >= i ? tables.getString(i++) : null);
                }

                result.add(new DbResourceImpl(functionName, this, new DbUri(name(),
                        asList(tableSchem, functionName)), props));
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
