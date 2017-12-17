package com.github.mgramin.sqlboot.sql.impl;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.sql.SqlQuery;
import java.util.Map;
import java.util.stream.Stream;

public class RestSqlQuery implements SqlQuery {

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
