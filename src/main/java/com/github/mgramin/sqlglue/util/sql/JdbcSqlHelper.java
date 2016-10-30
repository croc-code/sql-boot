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

    public JdbcSqlHelper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Map<String, String>> select(String sql) throws GlueException {
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
            throw new GlueException("SQL Exception", e);
        }
        return result;
    }

}
