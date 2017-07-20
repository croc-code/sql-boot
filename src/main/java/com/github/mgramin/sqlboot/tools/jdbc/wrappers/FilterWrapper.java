package com.github.mgramin.sqlboot.tools.jdbc.wrappers;

import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import lombok.ToString;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by MGramin on 18.07.2017.
 */
@ToString
public class FilterWrapper implements JdbcDbObjectType {

    private final JdbcDbObjectType jdbcDbObjectType;

    public FilterWrapper(JdbcDbObjectType jdbcDbObjectType) {
        this.jdbcDbObjectType = jdbcDbObjectType;
    }

    @Override
    public String name() {
        return jdbcDbObjectType.name();
    }

    @Override
    public List<Map<String, String>> read(List<String> params) throws SQLException {
        return null;
    }

}
