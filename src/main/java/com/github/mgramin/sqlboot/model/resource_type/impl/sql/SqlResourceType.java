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

package com.github.mgramin.sqlboot.model.resource_type.impl.sql;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource.impl.DbResourceImpl;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import com.github.mgramin.sqlboot.sql.select.SelectQuery;
import lombok.ToString;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.strip;

/**
 * Created by MGramin on 12.07.2017.
 */
@ToString
public class SqlResourceType implements ResourceType {

    private final transient SelectQuery selectQuery;
    private final List<String> aliases;

    public SqlResourceType(SelectQuery selectQuery, List<String> aliases) {
        this.selectQuery = selectQuery;
        this.aliases = aliases;
    }

    @Override
    public List<String> aliases() {
        return aliases;
    }

    @Override
    public List<String> path() {
        return selectQuery.metaData().keySet().stream()
            .filter(v -> v.startsWith("@"))
            .map(v -> strip(v, "@"))
            .collect(toList());
    }

    @Override
    public Stream<DbResource> read(final Uri uri) throws BootException {
        final Map<String, Object> variables = new HashMap<>();
        variables.put("uri", uri);
        return selectQuery.select(variables)
            .map(o -> {
                final List<Object> path = o.entrySet().stream()
                    .filter(v -> (v.getKey().startsWith("@")))
                    .map(Entry::getValue)
                    .collect(toList());

                final String name = path.get(path.size() - 1).toString();

                final Map<String, Object> headers = o.entrySet().stream()
                    .collect(toMap(
                        k -> strip(k.getKey(), "@"),
                        v -> ofNullable(v.getValue()).orElse(""),
                        (a, b) -> a,
                        LinkedHashMap::new));

                return new DbResourceImpl(name, this,
                    new DbUri(this.name(), path.stream().map(Object::toString).collect(toList())),
                    headers);
            });
    }

    @Override
    public Map<String, String> metaData() {
        return selectQuery.metaData();
    }

}
