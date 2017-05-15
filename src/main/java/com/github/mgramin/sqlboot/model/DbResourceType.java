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

import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.readers.DbResourceReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.ToString;

/**
 * Resource type of DB
 * e.g. "table", "index", "pk", "stored procedure", "session", "block" etc
 */
@ToString
public class DbResourceType {

    private final List<String> aliases;
    private final List<DbResourceType> child;
    private final List<DbResourceReader> readers;
    private final List<DbResourceTypeAggregator> aggregators;

    public List<DbResource> read(DbUri dbUri, DbResourceCommand command, String aggregatorName)
        throws SqlBootException {
        final DbResourceReader reader = this.readers().stream().findFirst().orElse(null);
        final List<DbResource> objects = reader.readr(dbUri, this);

        final List<DbResource> objectsNew = new ArrayList<>();
        for (DbResource object : objects) {
            if (object.type().equals(this) || dbUri.recursive()) {

                if (object.type().aggregators() != null) {
                    final DbResourceTypeAggregator objectTypeAggregator = object.type()
                        .aggregators()
                        .stream().filter(a -> a.name().contains(aggregatorName)).findFirst()
                        .orElse(null);
                    if (objectTypeAggregator != null) {
                        final ActionGenerator currentGenerator = object.type().aggregators()
                            .stream()
                            .filter(a -> a.name().contains(aggregatorName))
                            .findFirst()
                            .orElseGet(null)
                            .commands()
                            .stream()
                            .filter(c -> c.command().name().equalsIgnoreCase(
                                command.name()))
                            .findFirst()
                            .orElse(null);

                        if (currentGenerator != null) {
                            final ObjectService objectService = new ObjectService(objects,
                                String.join(".", object.dbUri()
                                    .objects()));
                            final Map<String, Object> variables = (Map) object.headers();
                            variables.put(object.type().name(), object);
                            variables.put("srv", objectService);

                            final DbResourceBodyWrapper dbResourceBodyWrapper = new DbResourceBodyWrapper(
                                object, currentGenerator.generate(variables));
                            objectsNew.add(dbResourceBodyWrapper);
                        }
                    }
                }
            }
        }
        return objectsNew;
    }

    public DbResourceType(String[] aliases, List<DbResourceType> child,
        List<DbResourceReader> readers, List<DbResourceTypeAggregator> aggregators) {
        this.aliases = asList(aliases);
        this.child = child;
        this.readers = readers;
        this.aggregators = aggregators;
    }

    public DbResourceType(String[] aliases, List<DbResourceReader> readers,
        List<DbResourceTypeAggregator> aggregators) {
        this.aliases = asList(aliases);
        this.child = null;
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