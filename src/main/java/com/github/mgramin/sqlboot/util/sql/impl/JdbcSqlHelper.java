package com.github.mgramin.sqlboot.util.sql.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maksim on 09.07.16.
 */
public class JdbcSqlHelper implements ISqlHelper {

    public DataSource dataSource;

    public JdbcSqlHelper() {
    }

    public JdbcSqlHelper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Map<String, String>> select(String sql) throws SqlBootException {
        List<Map<String, String>> result = new ArrayList<>();
        try (ResultSet rs = dataSource.getConnection().createStatement().executeQuery(sql)) {
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            while (rs.next()) {
                Map<String, String> map = new LinkedHashMap<>();
                for (int i = 1; i < columnCount + 1; i++) {
                    map.put(rsMetaData.getColumnName(i), rs.getString(i));
                }
                result.add(map);
            }
        } catch (SQLException e) {
            throw new SqlBootException("SQL Exception", e);
        }
        return result;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
