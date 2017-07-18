package com.github.mgramin.sqlboot.tools.jdbc.impl;

import com.github.mgramin.sqlboot.tools.jdbc.CustomResultSet;
import com.github.mgramin.sqlboot.tools.jdbc.CustomResultSetImpl;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

/**
 * Created by MGramin on 13.07.2017.
 */
public class PrimaryKey implements JdbcDbObjectType {

    private final DataSource dataSource;
    private final CustomResultSet customResultSet;

    public PrimaryKey(final DataSource dataSource) {
        this(dataSource, new CustomResultSetImpl());
    }

    public PrimaryKey(final DataSource dataSource, CustomResultSet customResultSet) {
        this.dataSource = dataSource;
        this.customResultSet = customResultSet;
    }

    @Override
    public String name() {
        return "pk";
    }

    @Override
    public List<Map<String, String>> read(List<String> params) throws SQLException {
        ResultSet columns = dataSource.getConnection().getMetaData().
            getPrimaryKeys(null, params.get(0), params.get(1));
        return customResultSet.toMap(columns);
    }

}
