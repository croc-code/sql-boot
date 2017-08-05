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

package com.github.mgramin.sqlboot.model.resource_types.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.model.resource_types.ResourceTypes;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.JdbcResourceType;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import com.github.mgramin.sqlboot.tools.jdbc.impl.Column;
import com.github.mgramin.sqlboot.tools.jdbc.impl.ForeignKey;
import com.github.mgramin.sqlboot.tools.jdbc.impl.Function;
import com.github.mgramin.sqlboot.tools.jdbc.impl.Index;
import com.github.mgramin.sqlboot.tools.jdbc.impl.PrimaryKey;
import com.github.mgramin.sqlboot.tools.jdbc.impl.Procedure;
import com.github.mgramin.sqlboot.tools.jdbc.impl.Schema;
import com.github.mgramin.sqlboot.tools.jdbc.impl.Table;
import static java.util.Collections.singletonList;

/**
 * Created by MGramin on 11.07.2017.
 */
public class FsResourceTypes implements ResourceTypes {

    private final DataSource dataSource;
    private final List<ResourceType> result = new ArrayList<>();

    public FsResourceTypes(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void init() {
        walk("src/main/resources/conf/common/database");
    }

    @Override
    public ResourceType findByName(String name) {
        return result.stream().filter(v -> v.name().equalsIgnoreCase(name)).findAny().get();
    }

    private List<ResourceType> walk(final String path) {
        File[] files = new File(path).listFiles();
        if (files == null) return null;
        List<ResourceType> list = new ArrayList<>();
        for (File f : files) {
            if (f.isDirectory()) {
                List<ResourceType> child = walk(f.getAbsolutePath());
                final JdbcDbObjectType jdbcDbObjectType;
                switch (f.getName()) {
                    case "schema" :  jdbcDbObjectType = new Schema(dataSource); break;
                    case "table" : jdbcDbObjectType = new Table(dataSource); break;
                    case "column" :  jdbcDbObjectType = new Column(dataSource); break;
                    case "pk" :  jdbcDbObjectType = new PrimaryKey(dataSource); break;
                    case "fk" :  jdbcDbObjectType = new ForeignKey(dataSource); break;
                    case "index" :  jdbcDbObjectType = new Index(dataSource); break;
                    case "procedure" :  jdbcDbObjectType = new Procedure(dataSource); break;
                    case "function" :  jdbcDbObjectType = new Function(dataSource); break;
                    default: jdbcDbObjectType = null;
                }
                if (jdbcDbObjectType != null) {
                    ResourceType resourceType = new JdbcResourceType(singletonList(f.getName()), child, jdbcDbObjectType);
                    result.add(resourceType);
                    list.add(resourceType);
                }
            }
        }
        return list;
    }

}
