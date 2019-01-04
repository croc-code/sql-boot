/*
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2019 Maksim Gramin
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
