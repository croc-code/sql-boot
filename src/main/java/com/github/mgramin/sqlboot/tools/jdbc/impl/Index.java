package com.github.mgramin.sqlboot.tools.jdbc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObject;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectImpl;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import lombok.ToString;
import static java.util.Arrays.asList;

/**
 * Created by MGramin on 13.07.2017.
 */
@ToString
public class Index extends AbstractJdbcObjectType implements JdbcDbObjectType {

    private final DataSource dataSource;

    public Index(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String name() {
        return "index";
    }

    @Override
    public List<JdbcDbObject> read(List<String> params) throws SQLException {
        ResultSet indexes = dataSource.getConnection().getMetaData().
                getIndexInfo(null, "%", "%", false, false);
        return toMap(indexes).stream()
                .map(v -> new JdbcDbObjectImpl(
                        asList(v.get("TABLE_SCHEMA"), v.get("TABLE_NAME"),
                                v.get("INDEX_NAME")), v))
                .collect(Collectors.toList());
    }

}
