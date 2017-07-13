package com.github.mgramin.sqlboot.tools.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
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
        final String schema = params.get(0);
        final String table = params.get(1);
        final String column = params.get(2);
        final List<Map<String, String>> result = new ArrayList<>();
        try (final Connection connection = dataSource.getConnection()) {
            final DatabaseMetaData metaData = connection.getMetaData();
            final ResultSet columns = metaData.getColumns(null, schema, table, column);
            while (columns.next()) {
                final Map<String, String> map = new HashMap<>();
                map.put("COLUMN_NAME", columns.getString("COLUMN_NAME"));
                map.put("TABLE_NAME", columns.getString("TABLE_NAME"));
                result.add(map);
            }
        }
        return result;
    }
}
