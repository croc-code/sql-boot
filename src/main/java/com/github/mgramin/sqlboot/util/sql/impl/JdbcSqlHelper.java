/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 mgramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.github.mgramin.sqlboot.util.sql.impl;

import static java.util.Collections.singletonList;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class JdbcSqlHelper implements ISqlHelper {

    public JdbcSqlHelper(List<DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    private List<DataSource> dataSources;



    @Override
    public List<Map<String, String>> select(String sql) throws SqlBootException {
        return selectBatch(singletonList(sql));
    }

    @Override
    public List<Map<String, String>> selectBatch(List<String> sql) throws SqlBootException {
        List<Map<String, String>> result = new ArrayList<>();

        try (Connection connection = dataSources.get(0).getConnection()) {
            for (String s : sql) {
                if (s.toLowerCase().startsWith("select")) {
                    try (ResultSet resultSet = connection.createStatement().executeQuery(s)) {
                        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                        int columnCount = resultSetMetaData.getColumnCount();
                        while (resultSet.next()) {
                            Map<String, String> map = new LinkedHashMap<>();
                            for (int i = 1; i < columnCount + 1; i++) {
                                map.put(resultSetMetaData.getColumnName(i), resultSet.getString(i));
                            }
                            result.add(map);
                        }
                    }
                } else {
                    try (Statement statement = connection.createStatement()) {
                        statement.execute(s);
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new SqlBootException("SQL Exception", e);
        }
        return result;
    }


}
