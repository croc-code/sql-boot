package com.github.mgramin.sqlboot.tools.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by MGramin on 13.07.2017.
 */
public interface DbObject {

    String name();

    List<Map<String, String>> get(List<String> params) throws SQLException;

}