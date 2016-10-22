package com.github.mgramin.sqlglue.util.sql;

import com.github.mgramin.sqlglue.exceptions.GlueException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Created by maksim on 09.07.16.
 */
public class JdbcSqlHelper implements ISqlHelper {

    private DataSource dataSource;

    public List<Map<String, String>> select(String sql) throws GlueException {
        List<Map<String, String>> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> map = new LinkedHashMap<>();
                for (int ii = 1; ii < columnCount + 1; ii++) {
                    String columnName = resultSetMetaData.getColumnName(ii)/*.toLowerCase()*/;
                    String columnValue = resultSet.getString(columnName)/*.toLowerCase()*/;
                    map.put(columnName, columnValue);
                }
                result.add(map);
            }
        } catch (SQLException e) {
            throw new GlueException("");
        }

        return result;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
