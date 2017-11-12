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

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.strip;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource.impl.DbResourceImpl;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import com.github.mgramin.sqlboot.sql.SqlQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.ToString;
import org.apache.log4j.Logger;

/**
 * Created by MGramin on 12.07.2017.
 */
@ToString
public class SqlResourceType implements ResourceType {

    private final static Logger logger = Logger.getLogger(SqlResourceType.class);

    private final transient SqlQuery sqlHelper;
    private final List<String> aliases;
    private final List<ResourceType> child;
    private final String sql;

    public SqlResourceType(SqlQuery sqlHelper, List<String> aliases, String sql) {
        this(sqlHelper, aliases, null, sql);
    }

    public SqlResourceType(SqlQuery sqlHelper, List<String> aliases, List<ResourceType> child,
        String sql) {
        this.sqlHelper = sqlHelper;
        this.aliases = aliases;
        this.child = child;
        this.sql = sql;
    }

    @Override
    public String name() {
        return aliases.get(0);
    }

    @Override
    public List<String> aliases() {
        return aliases;
    }

    @Override
    public Stream<DbResource> read(Uri uri) throws BootException {
        logger.debug(sql);
        Stream<Map<String, String>> selectStream = sqlHelper.select(sql);
        return selectStream
            .map(v -> {
                final List<String> objectsForUri = new ArrayList<>();
                final HashMap<String, String> objectHeaders = new LinkedHashMap<>();
                for (Map.Entry<String, String> column : v.entrySet()) {
                    if (column.getKey().startsWith("@")) {
                        objectsForUri.add(column.getValue());
                    }
                    objectHeaders
                        .put(strip(column.getKey(), "@"), ofNullable(column.getValue()).orElse(""));
                }
                final String objectName = objectsForUri.get(objectsForUri.size() - 1);
                return new DbResourceImpl(objectName, this,
                    new DbUri(this.name(), objectsForUri),
                    objectHeaders);
            });

    }

}
