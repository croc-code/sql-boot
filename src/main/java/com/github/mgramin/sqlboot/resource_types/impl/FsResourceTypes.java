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

package com.github.mgramin.sqlboot.resource_types.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.resource_type.ResourceType;
import com.github.mgramin.sqlboot.resource_type.impl.jdbc.JdbcResourceType;
import com.github.mgramin.sqlboot.resource_types.ResourceTypes;
import com.github.mgramin.sqlboot.tools.jdbc.impl.Table;
import static java.util.Collections.singletonList;

/**
 * Created by MGramin on 11.07.2017.
 */
public class FsResourceTypes implements ResourceTypes {

    final private DataSource dataSource;

    public FsResourceTypes(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<ResourceType> load() {
        return walk("src/main/resources/conf/common/database");
    }

    private List<ResourceType> walk(final String path) {
        File[] files = new File(path).listFiles();
        if (files == null) return null;
        List<ResourceType> list = new ArrayList<>();
        for (File f : files) {
            if (f.isDirectory()) {
                System.out.println(f);
                List<ResourceType> child = walk(f.getAbsolutePath());
                ResourceType resourceType = new JdbcResourceType(singletonList(f.getName()), child, new Table(dataSource));
                list.add(resourceType);
            }
        }
        return list;
    }

}
