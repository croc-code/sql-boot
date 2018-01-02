package com.github.mgramin.sqlboot.tools.jdbc;

import java.util.List;
import java.util.Map;

public interface JdbcDbObject {

    /**
     *
     * @return
     */
    String name();

    /**
     *
     * @return
     */
    List<String> path();

    /**
     *
     * @return
     */
    Map<String, Object> properties();

}
