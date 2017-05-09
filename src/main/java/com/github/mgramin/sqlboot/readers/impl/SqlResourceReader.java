/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 mgramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.github.mgramin.sqlboot.readers.impl;

import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.model.DbResourceThin;
import com.github.mgramin.sqlboot.model.DbResourceType;
import com.github.mgramin.sqlboot.model.DbUri;
import com.github.mgramin.sqlboot.readers.AbstractResourceReader;
import com.github.mgramin.sqlboot.readers.DbResourceReader;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;
import lombok.ToString;
import org.apache.log4j.Logger;

import java.util.*;

import static java.util.Optional.ofNullable;

/**
 * Custom-SQL db object reader
 */
@ToString
public class SqlResourceReader extends AbstractResourceReader implements DbResourceReader {

    final private static Logger logger = Logger.getLogger(SqlResourceReader.class);

    final private ISqlHelper sqlHelper;
    final private ActionGenerator actionGenerator;

    public SqlResourceReader(ISqlHelper sqlHelper, ActionGenerator actionGenerator) {
        this.sqlHelper = sqlHelper;
        this.actionGenerator = actionGenerator;
    }

    @Override
    public Map<String, DbResource> read(DbUri dbUri, DbResourceType type) throws SqlBootException {
        final Map<String, DbResource> objects = new LinkedHashMap<>();
        final String sql = actionGenerator.generate(new ArrayList<>(dbUri.objects()));
        final List<Map<String, String>> select = sqlHelper.select(sql);

        logger.debug(sql);

        for (Map<String, String> row : select) {
            final List<String> objectsForUri = new ArrayList<>();
            String objectName = null;
            final Properties objectHeaders = new Properties();
            for (Map.Entry<String, String> column : row.entrySet()) {
                if (!column.getKey().startsWith("@")) {
                    objectsForUri.add(column.getValue());
                    objectName = column.getValue();
                    objectHeaders.put(column.getKey(), column.getValue());
                } else {
                    objectHeaders.put(column.getKey().substring(1), ofNullable(column.getValue()).orElse(""));
                }
            }
            final DbResource object = new DbResourceThin(objectName, type, new DbUri(type.name(), objectsForUri), objectHeaders);
            objects.put(object.dbUri().toString(), object);
            logger.debug("find object " + object.dbUri().toString());
        }
        return objects;
    }

}