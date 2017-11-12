/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Maksim Gramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.mgramin.sqlboot.sql.impl;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.StreamSupport.stream;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.sql.SqlQuery;
import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * Execute SQL-query through plain old Jdbc.
 *
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class JdbcSqlQuery implements SqlQuery {

    /**
     * Data source.
     */
    private final DataSource dataSource;

    /**
     * Ctor.
     *
     * @param datasource Data source
     */
    public JdbcSqlQuery(final DataSource datasource) {
        this.dataSource = datasource;
    }

    @Override
    public Stream<Map<String, String>> select(String sql) throws BootException {
        final SqlRowSet rowSet = new JdbcTemplate(dataSource).queryForRowSet(sql);
        Iterator<Map<String, String>> iterator = new Iterator<Map<String, String>>() {
            @Override
            public boolean hasNext() {
                return rowSet.next();
            }

            @Override
            public Map<String, String> next() {
                return stream(rowSet.getMetaData().getColumnNames())
                    .map(v -> new SimpleEntry<>(v, rowSet.getString(v)))
                    .collect(toMap(Entry::getKey, v -> ofNullable(v.getValue())
                        .map(Object::toString)
                        .orElse("[NULL]"), (a, b) -> a, LinkedHashMap::new));
            }
        };
        return stream(spliteratorUnknownSize(iterator, ORDERED), false);
    }

    @Override
    public void dbHealth() {
        new JdbcTemplate(dataSource);
    }

}
