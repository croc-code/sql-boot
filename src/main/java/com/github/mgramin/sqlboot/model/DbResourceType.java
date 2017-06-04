/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Maksim Gramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.readers.DbResourceReader;
import lombok.ToString;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

/**
 * Resource type of DB
 * e.g. "table", "index", "pk", "stored procedure", "session", "block" etc
 */
@ToString
@JsonSerialize(as=DbResourceType.class)
public final class DbResourceType implements ResourceType {

    private final List<String> aliases;
    private final transient List<DbResourceType> child;
    private final transient List<DbResourceReader> readers;
    private final transient List<ActionGenerator> generators;

    public DbResourceType(String[] aliases, List<DbResourceReader> readers) {
        this(aliases, null, readers, null);
    }

    public DbResourceType(String[] aliases, List<DbResourceReader> readers, List<ActionGenerator> generators) {
        this(aliases, null, readers, generators);
    }

    public DbResourceType(String[] aliases, List<DbResourceType> child, List<DbResourceReader> readers, List<ActionGenerator> generators) {
        this.aliases = asList(aliases);
        this.child = child;
        this.readers = readers;
        this.generators = generators;
    }

    @Override
    public String name() {
        return this.aliases.get(0);
    }

    @Override
    public List<String> aliases() {
        return this.aliases;
    }

    @Override
    public List<ActionGenerator> generators() {
        return generators;
    }

    @Override
    public List<DbResource> read(Uri uri, IDbResourceCommand command, String aggregatorName) throws BootException {
        List<DbResource> objects = read(uri);
        final List<DbResource> objectsNew = new ArrayList<>();
        for (final DbResource dbResource : objects) {
            if (dbResource.type().equals(this) || uri.recursive()) {
/*                Map<DbResourceType, List<DbResource>> objectsByType =
                        objects.stream().collect(Collectors.groupingBy(DbResource::type));*/
                final ObjectService objectService = new ObjectService(objects,
                        String.join(".", dbResource.dbUri()
                                .objects()));
                final Map<String, Object> variables = new HashMap<>((Map) dbResource.headers());
                variables.put(dbResource.type().name(), dbResource);
                variables.put("srv", objectService);
                /*for (Map.Entry<DbResourceType, List<DbResource>> entry : objectsByType.entrySet()) {
                    if (!entry.getKey().name().equals(dbResource.type().name())) {
                        variables.put(entry.getKey().name() + "_",
                                entry.getValue()
                                        .stream()
                                        .filter(a -> a.dbUri().toString().startsWith(entry.getKey().name() + "/" + String.join(".", dbResource.dbUri().objects())))
                        );
                    }
                }*/
                final ActionGenerator generator;
                if (dbResource.type().generators() != null) {
                    generator = dbResource.type().generators().stream()
                            .filter(g -> g.command().equals(command) && g.aggregators().contains(aggregatorName))
                            .findFirst().orElse(null);
                } else {
                    generator = null;
                }
                if (generator != null) {
                    final DbResourceBodyWrapper dbResourceBodyWrapper = new DbResourceBodyWrapper(
                            dbResource, generator.generate(variables));
                    objectsNew.add(dbResourceBodyWrapper);
                } else {
                    objectsNew.add(dbResource);
                }
            }
        }
        return objectsNew;
    }

    public List<DbResource> read(Uri uri) throws BootException {
        final DbResourceReader reader = this.readers.stream().findFirst().orElse(null);
        final List<DbResource> objects = reader.read(uri, this);
        ofNullable(this.child).ifPresent(c -> c.forEach(a -> objects.addAll(a.read(uri))));
        return objects;
    }

}