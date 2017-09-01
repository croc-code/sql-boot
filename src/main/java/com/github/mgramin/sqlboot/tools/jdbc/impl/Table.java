package com.github.mgramin.sqlboot.tools.jdbc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObject;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectImpl;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import lombok.ToString;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

/**
 * Created by MGramin on 13.07.2017.
 */
@ToString
public class Table extends AbstractJdbcObjectType implements JdbcDbObjectType {

    private static final String COLUMN_NAME_PROPERTY = "TABLE_NAME";
    private final DataSource dataSource;

    public Table(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String name() {
        return "table";
    }

    @Override
    public List<JdbcDbObject> read(List<String> params) throws SQLException {
        ResultSet columns = dataSource.getConnection().getMetaData().
                getTables(null, "%", "%", new String[]{"TABLE"});
        return toMap(columns).stream()
                .map(v -> new JdbcDbObjectImpl(
                        asList(v.get("TABLE_SCHEMA"), v.get(COLUMN_NAME_PROPERTY)),
                        v))
                .collect(toList());
    }

}
