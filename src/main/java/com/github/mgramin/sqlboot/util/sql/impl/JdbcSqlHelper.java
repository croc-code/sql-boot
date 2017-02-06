package com.github.mgramin.sqlboot.util.sql.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Created by maksim on 09.07.16.
 */
public class JdbcSqlHelper implements ISqlHelper {

    private DataSource dataSource;

    public JdbcSqlHelper() {
    }

    public JdbcSqlHelper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Map<String, String>> select(String sql) throws SqlBootException {
        return selectBatch(Arrays.asList(sql));
    }

    @Override
    public List<Map<String, String>> selectBatch(List<String> sql) throws SqlBootException {
        List<Map<String, String>> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
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


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
