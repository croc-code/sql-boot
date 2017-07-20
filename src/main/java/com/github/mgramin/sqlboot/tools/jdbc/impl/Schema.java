package com.github.mgramin.sqlboot.tools.jdbc.impl;

import com.github.mgramin.sqlboot.tools.jdbc.CustomResultSet;
import com.github.mgramin.sqlboot.tools.jdbc.CustomResultSetImpl;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import lombok.ToString;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

/**
 * Created by MGramin on 13.07.2017.
 */
@ToString
public class Schema implements JdbcDbObjectType {

    private final DataSource dataSource;
    private final CustomResultSet customResultSet;

    public Schema(final DataSource dataSource) {
        this(dataSource, new CustomResultSetImpl());
    }

    public Schema(final DataSource dataSource, CustomResultSet customResultSet) {
        this.dataSource = dataSource;
        this.customResultSet = customResultSet;
    }

    @Override
    public String name() {
        return "schema";
    }

    @Override
    public List<Map<String, String>> read(List<String> params) throws SQLException {
        ResultSet schemas = dataSource.getConnection().getMetaData().
            getSchemas(null, params.get(0));
        return customResultSet.toMap(schemas);
    }

}
