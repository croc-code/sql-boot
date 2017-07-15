package com.github.mgramin.sqlboot.tools.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by MGramin on 15.07.2017.
 */
public abstract class DbObjectAbstract implements DbObject {

    @Override
    public List<Map<String, String>> get(List<String> params) throws SQLException {
        return null;
    }
}
