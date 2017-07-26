package com.github.mgramin.sqlboot.tools.jdbc.impl;

import static java.util.Collections.singletonList;
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
public class Schema implements JdbcDbObjectType {

    private static final String COLUMN_NAME_PROPERTY = "SCHEMA_NAME";
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
    public List<JdbcDbObject> read(List<String> params) throws SQLException {
        ResultSet schemas = dataSource.getConnection().getMetaData().
            getSchemas(null, params.get(0));
        return customResultSet.toMap(schemas).stream()
            .map(v -> new JdbcDbObjectImpl(v.get(COLUMN_NAME_PROPERTY),
                singletonList(v.get(COLUMN_NAME_PROPERTY)), v))
            .collect(toList());
    }

}
