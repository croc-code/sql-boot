package com.github.mgramin.sqlboot.sql.select.wrappers;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.sql.select.SelectQuery;

import java.util.Map;
import java.util.stream.Stream;

public class PageWrapper implements SelectQuery {

    private final SelectQuery selectQuery;
    private final int pageNumber;
    private final int pageSize;

    /**
     * Ctor.
     *
     * @param selectQuery
     * @param pageNumber
     * @param pageSize
     */
    public PageWrapper(final SelectQuery selectQuery,
                       final int pageNumber,
                       final int pageSize
    ) {
        this.selectQuery = selectQuery;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }


    @Override
    public Stream<Map<String, Object>> select() throws BootException {
        String sqlQuery = selectQuery.getQuery();
        return null;
    }

    @Override
    public Map<String, String> columns() {
        return null;
    }

    @Override
    public void dbHealth() throws BootException {

    }

}
