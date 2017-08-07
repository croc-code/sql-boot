package com.github.mgramin.sqlboot.tools.jdbc;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by MGramin on 13.07.2017.
 */
public interface JdbcDbObjectType {

    String name();

    List<JdbcDbObject> read(final List<String> params) throws SQLException;

}
