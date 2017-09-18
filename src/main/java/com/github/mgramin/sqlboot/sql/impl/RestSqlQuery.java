package com.github.mgramin.sqlboot.sql.impl;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.sql.SqlQuery;
import java.util.List;
import java.util.Map;

public class RestSqlQuery implements SqlQuery {

    @Override
    public List<Map<String, String>> select(String sql) throws BootException {
        return null;
    }

    @Override
    public void dbHealth() throws BootException {

    }

}
