/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2017 Maksim Gramin
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

package com.github.mgramin.sqlboot.model.resource_type.impl.composite;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.connection.DbConnection;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.composite.md.MarkdownFile;
import com.github.mgramin.sqlboot.model.resource_type.impl.sql.SqlResourceType;
import com.github.mgramin.sqlboot.model.resource_type.wrappers.body.TemplateBodyWrapper;
import com.github.mgramin.sqlboot.model.resource_type.wrappers.header.SelectWrapper;
import com.github.mgramin.sqlboot.model.resource_type.wrappers.list.LimitWrapper;
import com.github.mgramin.sqlboot.model.resource_type.wrappers.list.PageWrapper;
import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.sql.select.impl.JdbcSelectQuery;
import com.github.mgramin.sqlboot.template.generator.impl.GroovyTemplateGenerator;

import java.util.Arrays;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonList;
import static org.apache.commons.io.FileUtils.readFileToString;

/**
 * Created by MGramin on 11.07.2017.
 */
public class FsResourceTypes implements ResourceType {

    /**
     *
     */
    private final DataSource dataSource;

    /**
     *
     */
    private final List<ResourceType> resourceTypes;

    /**
     * Ctor.
     *
     * @param dbConnection
     * @throws BootException
     */
    public FsResourceTypes(final DbConnection dbConnection, final Uri uri) throws BootException {
        dataSource = dbConnection.getDataSource();
        try {
            final String baseFolder = dbConnection.getBaseFolder().getFile().getPath();
            resourceTypes = walk(baseFolder, uri);
        } catch (IOException e) {
            throw new BootException(e);
        }
    }

    @Deprecated
    public List<ResourceType> resourceTypes() {
        return resourceTypes;
    }

    /**
     *
     * @param path
     * @param uri
     * @return
     */
    private List<ResourceType> walk(final String path, Uri uri) {
        File[] files = new File(path).listFiles();
        if (files == null) return null;
        List<ResourceType> list = new ArrayList<>();
        for (File f : files) {
            if (f.isDirectory()) {
                File sqlFile = new File(f, "README.md");
                list.addAll(walk(f.getAbsolutePath(), uri));

                String sql = null;
                try {
                    final MarkdownFile markdownFile = new MarkdownFile(readFileToString(sqlFile, UTF_8));
                    final Map<String, String> parse = markdownFile.parse();

                    if (uri != null && uri.action() != null) {
                        String s = parse.get(uri.action());
                        if (s != null) {
                            sql = s;
                        } else {
                            final Iterator<Map.Entry<String, String>> iterator = parse.entrySet().iterator();
                            if (iterator.hasNext()) {
                                sql = iterator.next().getValue();
                            }
                        }
                    } else {
                        final Iterator<Map.Entry<String, String>> iterator = parse.entrySet().iterator();
                        if (iterator.hasNext()) {
                            sql = iterator.next().getValue();
                        }
                    }

                } catch (IOException e) {
                    // TODO catch and process this exception
                }


                final ResourceType baseResourceType;
                if (sqlFile.exists() && sql != null) {
                    baseResourceType = new SqlResourceType(
                        new JdbcSelectQuery(
                            dataSource, new GroovyTemplateGenerator(sql)), singletonList(f.getName()));
                } else {
                    baseResourceType = null;
                }
                final ResourceType resourceType = //new CacheWrapper(
                    new SelectWrapper(
//                    new SqlBodyWrapper(
                        new TemplateBodyWrapper(
                            new PageWrapper(
                                new LimitWrapper(
//                                new WhereWrapper(
                                    baseResourceType)
                                ),
                            new GroovyTemplateGenerator("EMPTY BODY ..."))
//                        dataSource)
                );
                if (baseResourceType != null) {
                    list.add(resourceType);
                }
            }
        }
        return list;
    }

    @Override
    public List<String> aliases() {
        throw new BootException("Not implemented!");
    }

    @Override
    public List<String> path() {
        throw new BootException("Not implemented!");
    }

    @Override
    public Stream<DbResource> read(final Uri uri) throws BootException {
        return resourceTypes.stream()
                .filter(v -> v.name().equalsIgnoreCase(uri.type()))
                .findAny()
                .get()
                .read(uri);
    }

    @Override
    public Map<String, String> metaData() {
        throw new BootException("Not implemented!");
    }

}
