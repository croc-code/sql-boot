package com.github.mgramin.sqlboot.tools.jdbc.impl;

import com.github.mgramin.sqlboot.tools.jdbc.DbObject;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

/**
 * Created by MGramin on 13.07.2017.
 */
public class Column implements DbObject {

    final private DataSource dataSource;

    public Column(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String name() {
        return "column";
    }

    @Override
    public List<Map<String, String>> get(List<String> params) throws SQLException {
        final List<Map<String, String>> result = new ArrayList<>();
        try (final Connection connection = dataSource.getConnection()) {
            final DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = getDbObjectMatadata(params, metaData);
            ResultSetMetaData tableMetaData = tables.getMetaData();
            int columnCount = tableMetaData.getColumnCount();
            while (tables.next()) {
                Map<String, String> map = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(tableMetaData.getColumnName(i), tables.getString(i));
                }
                result.add(map);
            }
        }
        return result;
    }

    private ResultSet getDbObjectMatadata(List<String> params, DatabaseMetaData metaData) throws SQLException {
        return metaData.getColumns(null, params.get(0), params.get(1), params.get(2));
    }

}
