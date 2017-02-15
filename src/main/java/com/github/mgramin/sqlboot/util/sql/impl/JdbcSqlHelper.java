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

    private List<DataSource> dataSources; // TODO change to List<DataSource>



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
