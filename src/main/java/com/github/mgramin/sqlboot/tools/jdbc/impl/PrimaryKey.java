package com.github.mgramin.sqlboot.tools.jdbc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.tools.jdbc.CustomResultSet;
import com.github.mgramin.sqlboot.tools.jdbc.CustomResultSetImpl;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObject;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import lombok.ToString;

/**
 * Created by MGramin on 13.07.2017.
 */
@ToString
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
    public List<JdbcDbObject> read(List<String> params) throws SQLException {
        ResultSet columns = dataSource.getConnection().getMetaData().
            getPrimaryKeys(null, params.get(0), params.get(1));
        return null;
    }

}
