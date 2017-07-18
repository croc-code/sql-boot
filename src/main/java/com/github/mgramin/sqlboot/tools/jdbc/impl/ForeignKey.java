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
public class ForeignKey implements JdbcDbObjectType {

    private final DataSource dataSource;
    private final CustomResultSet customResultSet;

    public ForeignKey(final DataSource dataSource) {
        this(dataSource, new CustomResultSetImpl());
    }

    public ForeignKey(final DataSource dataSource, CustomResultSet customResultSet) {
        this.dataSource = dataSource;
        this.customResultSet = customResultSet;
    }

    @Override
    public String name() {
        return "fk";
    }

    @Override
    public List<Map<String, String>> read(List<String> params) throws SQLException {
        ResultSet foreignKeys = dataSource.getConnection().getMetaData().
            getImportedKeys(null, params.get(0), params.get(1));
        return customResultSet.toMap(foreignKeys);
    }

}
