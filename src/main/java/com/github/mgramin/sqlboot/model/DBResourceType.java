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

package com.github.mgramin.sqlboot.model;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import com.github.mgramin.sqlboot.readers.DbResourceReader;
import lombok.ToString;

import java.util.List;

/**
 * Resource type of DB
 * e.g. "table", "index", "pk", "stored procedure", "session", "block" etc
 */
@ToString
public class DBResourceType {

    public String name;
    public List<String> aliases;
    public String description;
    public List<DBResourceType> child;
    public List<DbResourceReader> readers;
    public List<DbSchemaObjectTypeAggregator> aggregators;

    /*Map<String, DBSchemaObject> read(ObjURI objURI) throws SqlBootException {
        return null;
    }*/

    public DBResourceType() {
    }

    public DBResourceType(String name, List<DBResourceType> child, List<DbResourceReader> readers) {
        this.name = name;
        this.child = child;
        this.readers = readers;
    }

    public DBResourceType(String name, List<DbResourceReader> reader) {
        this.name = name;
        this.readers = reader;
    }

    public DBResourceType(String name, DbResourceReader reader) {
        this.name = name;
        this.readers = singletonList(reader);
    }

    public DBResourceType(String name, List<DBResourceType> child, DbResourceReader reader) {
        this.name = name;
        this.child = child;
        this.readers = singletonList(reader);
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChild(List<DBResourceType> child) {
        this.child = child;
    }

    public void setReaders(List<DbResourceReader> readers) {
        this.readers = readers;
    }

    public void setAggregators(List<DbSchemaObjectTypeAggregator> aggregators) {
        this.aggregators = aggregators;
    }

    public void setAliases(String[] aliases) {
        this.aliases = asList(aliases);
    }

}