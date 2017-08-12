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

package com.github.mgramin.sqlboot.sql.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.sql.SqlQuery;

/**
 * Execute SQL-query through Jdbc.
 *
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class JdbcSqlQuery implements SqlQuery {

    /**
     * Data source.
     */
    private final List<DataSource> dataSource;

    /**
     * Ctor.
     *
     * @param datasources Data source
     */
    public JdbcSqlQuery(final List<DataSource> datasources) {
        this.dataSource = datasources;
    }

    @Override
    public List<Map<String, String>> select(final String sql)
    throws BootException {
        final List<Map<String, String>> result = new ArrayList<>();
        try (Connection connection = dataSource.get(0).getConnection()) {
            final Statement statement = connection.createStatement();
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                final ResultSetMetaData rsMetaData = resultSet.getMetaData();
                final int columnCount = rsMetaData.getColumnCount();
                while (resultSet.next()) {
                    final Map<String, String> map = new LinkedHashMap<>();
                    for (int i = 1; i < columnCount + 1; i++) {
                        map.put(rsMetaData.getColumnName(i),
                                resultSet.getString(i));
                    }
                    result.add(map);
                }
            }
        } catch (SQLException e) {
            throw new BootException("SQL Exception", e);
        }
        return result;
    }

    @Override
    public void dbHealth() {
        try {
            dataSource.get(0).getConnection();
        } catch (SQLException e) {
            throw new BootException(e);
        }
    }

}
