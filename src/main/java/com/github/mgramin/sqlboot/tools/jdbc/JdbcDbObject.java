package com.github.mgramin.sqlboot.tools.jdbc;

import java.util.List;
import java.util.Map;

public interface JdbcDbObject {

    String name();

    List<String> path();

    Map<String, Object> properties();

}
