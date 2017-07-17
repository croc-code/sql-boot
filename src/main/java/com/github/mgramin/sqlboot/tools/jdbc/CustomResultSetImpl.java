package com.github.mgramin.sqlboot.tools.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MGramin on 16.07.2017.
 */
public class CustomResultSetImpl implements CustomResultSet {

    @Override
    public List<Map<String, String>> toMap(ResultSet resultSet) throws SQLException {
        final List<Map<String, String>> result = new ArrayList<>();
        final ResultSetMetaData tableMetaData = resultSet.getMetaData();
        final int columnCount = tableMetaData.getColumnCount();
        while (resultSet.next()) {
            final Map<String, String> map = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                map.put(tableMetaData.getColumnName(i), resultSet.getString(i));
            }
            result.add(map);
        }
        return result;
    }
}
