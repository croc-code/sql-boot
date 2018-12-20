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

package com.github.mgramin.sqlboot.sql.select.impl;

import com.github.mgramin.sqlboot.sql.select.impl.parser.SelectStatement.Column;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.sql.select.SelectQuery;
import com.github.mgramin.sqlboot.sql.select.impl.parser.SelectStatementParser;
import com.github.mgramin.sqlboot.template.generator.TemplateGenerator;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.StreamSupport.stream;

/**
 * Execute SQL-query through plain old Jdbc.
 *
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class JdbcSelectQuery implements SelectQuery {

    private final static Logger logger = Logger.getLogger(JdbcSelectQuery.class);

    /**
     * Data source.
     */
    private final DataSource dataSource;

    /**
     * Sql query text.
     */
    private final String sql;

    /**
     * Ctor.
     *
     * Text alias for DB NULL value
     */
    private final String nullAlias;

    /**
     *
     */
    private final TemplateGenerator templateGenerator;

    /**
     * Ctor.
     *
     * @param datasource
     * @param templateGenerator
     */
    public JdbcSelectQuery(final DataSource datasource, 
                           final TemplateGenerator templateGenerator) {
        this(datasource, null, "[NULL]", templateGenerator);
    }

    /**
     * Ctor.
     *
     * @param datasource Data source
     */
    public JdbcSelectQuery(final DataSource datasource, 
                           final String sql) {
        this(datasource, sql, "[NULL]", null);
    }

    /**
     * Ctor.
     *
     */
    public JdbcSelectQuery(final DataSource dataSource, 
                           final String sql, 
                           final String nullAlias, 
                           final TemplateGenerator templateGenerator) {
        this.dataSource = dataSource;
        this.sql = sql;
        this.nullAlias = nullAlias;
        this.templateGenerator = templateGenerator;
    }

    @Override
    public Stream<Map<String, Object>> select() throws BootException {
        return getMapStream(sql);
    }

    @Override
    public Stream<Map<String, Object>> select(final Map<String, Object> variables) throws BootException {
        return getMapStream(templateGenerator.generate(variables));
    }

    private Stream<Map<String, Object>> getMapStream(final String sqlText) {
        logger.info(sqlText);
        final SqlRowSet rowSet = new JdbcTemplate(dataSource).queryForRowSet(sqlText);
        Iterator<Map<String, Object>> iterator = new Iterator<Map<String, Object>>() {
            @Override
            public boolean hasNext() {
                return rowSet.next();
            }

            @Override
            public Map<String, Object> next() {
                return stream(rowSet.getMetaData().getColumnNames())
                    .map(v -> {
                        Object object = rowSet.getObject(v);
                        if (object instanceof SerialClob) {
                            try {
                                return new SimpleEntry<>(v, (Object)((SerialClob) object).getSubString(1,
                                    (int) ((SerialClob) object).length()));
                            } catch (SerialException e) {
                                e.printStackTrace();
                            }
                        }
                        return new SimpleEntry<>(v, object);
                    })
                    .collect(toMap(
                        k->k.getKey().toLowerCase(),
                        v -> ofNullable(v.getValue()).orElse(nullAlias),
                        (a, b) -> a,
                        LinkedHashMap::new));
            }
        };
        return stream(spliteratorUnknownSize(iterator, ORDERED), false);
    }

    @Override
    public Map<String, String> columns() {
        return new SelectStatementParser(templateGenerator.template())
                .parse()
                .columns()
                .stream()
                .collect(toMap(Column::name, Column::comment, (a, b) -> a, LinkedHashMap::new));
    }

    @Override
    public void dbHealth() {
        try {
            Connection connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new BootException(e);
        }
    }

    @Override
    public String getQuery() {
        return sql;
    }

}
