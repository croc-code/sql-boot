package com.github.mgramin.sqlboot.tools.jdbc;

import java.util.List;
import java.util.Map;
import lombok.ToString;

@ToString
public class JdbcDbObjectImpl implements JdbcDbObject {

    private final String name;
    private final List<String> path;
    private final Map<String, String> properties;

    public JdbcDbObjectImpl(String name, List<String> path, Map<String, String> properties) {
        this.name = name;
        this.path = path;
        this.properties = properties;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public List<String> path() {
        return path;
    }

    @Override
    public Map<String, String> properties() {
        return properties;
    }

}
