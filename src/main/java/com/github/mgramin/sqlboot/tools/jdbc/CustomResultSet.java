package com.github.mgramin.sqlboot.tools.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by MGramin on 16.07.2017.
 */
@Deprecated
public interface CustomResultSet {

    List<Map<String, String>> toMap(ResultSet resultSet) throws SQLException;

}
