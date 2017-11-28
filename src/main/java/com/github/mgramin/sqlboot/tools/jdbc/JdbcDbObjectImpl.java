package com.github.mgramin.sqlboot.tools.jdbc;

import java.util.List;
import java.util.Map;
import lombok.ToString;

@ToString
public class JdbcDbObjectImpl implements JdbcDbObject {

    private final String name;
    private final List<String> path;
    private final Map<String, Object> properties;

    public JdbcDbObjectImpl(List<String> path, Map<String, Object> properties) {
        this.name = path.get(path.size()-1);
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
    public Map<String, Object> properties() {
        return properties;
    }

}
