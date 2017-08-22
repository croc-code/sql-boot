package com.github.mgramin.sqlboot.tools.jdbc.impl;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

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
public class Column extends AbstractJdbcObjectType implements JdbcDbObjectType {

    private static final String COLUMN_NAME_PROPERTY = "COLUMN_NAME";
    private final DataSource dataSource;

    public Column(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String name() {
        return "column";
    }

    @Override
    public List<JdbcDbObject> read(List<String> params) throws SQLException {
        ResultSet columns = dataSource.getConnection().getMetaData()
                .getColumns(null, "%", "%", "%");
        return toMap(columns).stream()
            .map(v -> new JdbcDbObjectImpl(asList(v.get("TABLE_SCHEMA"), v.get("TABLE_NAME"),
                    v.get(COLUMN_NAME_PROPERTY)), v))
            .collect(toList());
    }

}
