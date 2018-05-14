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

package com.github.mgramin.sqlboot.model.resource_type.wrappers.body;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource.wrappers.DbResourceBodyWrapper;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.sql.select.impl.JdbcSelectQuery;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.sql.DataSource;

/**
 * Created by MGramin on 18.07.2017.
 */
public class SqlBodyWrapper implements ResourceType {

    private final ResourceType origin;
    private final DataSource dataSource;

    public SqlBodyWrapper(ResourceType origin, DataSource dataSource) {
        this.origin = origin;
        this.dataSource = dataSource;
    }

    @Override
    public String name() {
        return origin.name();
    }

    @Override
    public List<String> aliases() {
        return origin.aliases();
    }

    @Override
    public List<String> path() {
        return origin.path();
    }

    @Override
    public Stream<DbResource> read(final Uri uri) throws BootException {
        return origin.read(uri)
            .map(
                origin -> {
                    if (origin.body() != null && !origin.body().isEmpty()) {
                        final JdbcSelectQuery jdbcSqlQuery = new JdbcSelectQuery(dataSource,
                            origin.body());
                        final Map<String, Object> stringObjectMap = jdbcSqlQuery.select()
                            .findFirst().get();
                        final Object body = stringObjectMap.values().iterator().next();
                        return new DbResourceBodyWrapper(origin, body.toString());
                    }
                    return new DbResourceBodyWrapper(origin, "NOT SET SQL");
                }
            );
    }

    @Override
    public Map<String, String> metaData() {
        return origin.metaData();
    }

}
