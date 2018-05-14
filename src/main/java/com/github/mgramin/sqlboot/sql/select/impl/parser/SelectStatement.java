package com.github.mgramin.sqlboot.sql.select.impl.parser;

import java.util.List;
import java.util.Map;

public class SelectStatement {

    /**
     * Table name in select
     */
    private final String fromTable;

    /**
     * Columns name in select
     */
    private final List<Column> columns;

    public SelectStatement(String fromTable, List<Column> columns) {
        this.fromTable = fromTable;
        this.columns = columns;
    }

    public String tableName() {
        return fromTable;
    }

    public List<Column> columns() {
        return columns;
    }

    public static class Column {

        private String name;
        private String comment;
        private Map<String, String> properties;

        public Column(String name, String comment, Map<String, String> properties) {
            this.name = name;
            this.comment = comment;
            this.properties = properties;
        }

        public String name() {
            return name;
        }

        public String comment() {
            return comment;
        }

        public Map<String, String> properties() {
            return properties;
        }

    }

}
