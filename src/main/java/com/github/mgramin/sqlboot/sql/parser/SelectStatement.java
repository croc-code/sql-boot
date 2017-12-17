package com.github.mgramin.sqlboot.sql.parser;

import java.util.Map;

public class SelectStatement {

    private final String tableName;
    private final Map<String, String> columns;

    public SelectStatement(String tableName, Map<String, String> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    public String tableName() {
        return tableName;
    }

    public Map<String, String> columns() {
        return columns;
    }
}
