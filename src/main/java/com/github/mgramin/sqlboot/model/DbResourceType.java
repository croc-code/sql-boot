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

import com.github.mgramin.sqlboot.readers.DbResourceReader;
import lombok.ToString;

import java.util.List;

/**
 * Resource type of DB
 * e.g. "table", "index", "pk", "stored procedure", "session", "block" etc
 */
@ToString
public class DbResourceType {

    private List<String> aliases;
    private List<DbResourceType> child;
    private List<DbResourceReader> readers;
    private List<DbResourceTypeAggregator> aggregators;

    // TODO
    /*Map<String, DBSchemaObject> read(DbUri dbUri) throws SqlBootException {
        return null;
    }*/

    public DbResourceType(String[] aliases, List<DbResourceType> child, List<DbResourceReader> readers, List<DbResourceTypeAggregator> aggregators) {
        this.aliases = asList(aliases);
        this.child = child;
        this.readers = readers;
        this.aggregators = aggregators;
    }

    public DbResourceType(String[] aliases, List<DbResourceReader> readers, List<DbResourceTypeAggregator> aggregators) {
        this.aliases = asList(aliases);
        this.readers = readers;
        this.aggregators = aggregators;
    }

    public String name() {
        return this.aliases.get(0);
    }

    public List<String> aliases() {
        return this.aliases;
    }

    public List<DbResourceType> child() {
        return this.child;
    }

    public List<DbResourceReader> readers() {
        return this.readers;
    }

    public List<DbResourceTypeAggregator> aggregators() {
        return this.aggregators;
    }

}