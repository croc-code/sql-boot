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
public class Table implements DbObject {

    final private DataSource dataSource;

    public Table(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String name() {
        return "table";
    }

    @Override
    public List<Map<String, String>> get(List<String> params) throws SQLException {
        final String schema = params.get(0);
        final String table = params.get(1);
        final List<Map<String, String>> result = new ArrayList<>();
        try (final Connection connection = dataSource.getConnection()) {
            final DatabaseMetaData metaData = connection.getMetaData();
            final String[] tableTypes = {"TABLE"/*,"VIEW","ALIAS","SYNONYM","GLOBAL TEMPORARY",
        "LOCAL TEMPORARY","SYSTEM TABLE"*/};
            ResultSet tables = metaData.getTables(null, schema, table, tableTypes);
            while (tables.next()) {
                Map<String, String> map = new HashMap<>();
                map.put("TABLE_CAT", tables.getString("TABLE_CAT"));
                map.put("TABLE_SCHEM", tables.getString("TABLE_SCHEM"));
                map.put("TABLE_NAME", tables.getString("TABLE_NAME"));
                // map.put("TABLE_TYPE",tables.getString("TABLE_TYPE "));
                map.put("TYPE_CAT", tables.getString("TYPE_CAT"));
                map.put("TYPE_SCHEM", tables.getString("TYPE_SCHEM"));
                map.put("TYPE_NAME", tables.getString("TYPE_NAME"));
                map.put("SELF_REFERENCING_COL_NAME", tables.getString("SELF_REFERENCING_COL_NAME"));
                map.put("REF_GENERATION", tables.getString("REF_GENERATION"));
                result.add(map);
            }
        }
        return result;
    }

}
