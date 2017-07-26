package com.github.mgramin.sqlboot.tools.jdbc.impl;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import com.github.mgramin.sqlboot.tools.jdbc.CustomResultSet;
import com.github.mgramin.sqlboot.tools.jdbc.CustomResultSetImpl;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObject;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectImpl;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.ToString;

/**
 * Created by MGramin on 13.07.2017.
 */
@ToString
public class Table implements JdbcDbObjectType {

    private static final String COLUMN_NAME_PROPERTY = "TABLE_NAME";
    private final DataSource dataSource;
    private final CustomResultSet customResultSet;

    public Table(final DataSource dataSource) {
        this(dataSource, new CustomResultSetImpl());
    }

    public Table(final DataSource dataSource, CustomResultSet customResultSet) {
        this.dataSource = dataSource;
        this.customResultSet = customResultSet;
    }

    @Override
    public String name() {
        return "table";
    }

    @Override
    public List<JdbcDbObject> read(List<String> params) throws SQLException {
        ResultSet columns = dataSource.getConnection().getMetaData().
            getTables(null, params.get(0), params.get(1), new String[]{"TABLE"});
        return customResultSet.toMap(columns).stream()
            .map(v -> new JdbcDbObjectImpl(v.get(COLUMN_NAME_PROPERTY),
                asList(v.get("SCHEMA_NAME"), v.get(COLUMN_NAME_PROPERTY)),
                v))
            .collect(toList());
    }

}
