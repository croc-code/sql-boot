package com.github.mgramin.sqlboot.sql.select.impl;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.sql.select.SelectQuery;
import java.util.Map;
import java.util.stream.Stream;

public class RestSelectQuery implements SelectQuery {

    @Override
    public Stream<Map<String, Object>> select() throws BootException {
        return null;
    }

    @Override
    public Map<String, String> metaData() {
        return null;
    }

    @Override
    public void dbHealth() throws BootException {

    }

}
