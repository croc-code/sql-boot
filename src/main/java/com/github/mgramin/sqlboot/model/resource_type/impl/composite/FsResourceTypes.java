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
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.SchemaJdbcResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.function.FunctionJdbcResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.procedure.ProcedureJdbcResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.table.TableJdbcResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.table.column.ColumnJdbcResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.table.fk.FkJdbcResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.table.index.IndexJdbcResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.table.pk.PkJdbcResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.table.relation.ChildTableJdbcResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.table.relation.ParentTableJdbcResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.schema.view.ViewJdbcResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.sql.SqlResourceType;
import com.github.mgramin.sqlboot.model.resource_type.wrappers.body.TemplateBodyWrapper;
import com.github.mgramin.sqlboot.model.resource_type.wrappers.header.SelectWrapper;
import com.github.mgramin.sqlboot.model.resource_type.wrappers.list.LimitWrapper;
import com.github.mgramin.sqlboot.model.resource_type.wrappers.list.PageWrapper;
import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.sql.impl.JdbcSqlQuery;
import com.github.mgramin.sqlboot.template.generator.impl.GroovyTemplateGenerator;

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
    public FsResourceTypes(final DbConnection dbConnection) throws BootException {
        dataSource = dbConnection.getDataSource();
        try {
            final String baseFolder = dbConnection.getBaseFolder().getFile().getPath();
            resourceTypes = walk(baseFolder);
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
     * @return
     */
    private List<ResourceType> walk(final String path) {
        File[] files = new File(path).listFiles();
        if (files == null) return null;
        List<ResourceType> list = new ArrayList<>();
        for (File f : files) {
            if (f.isDirectory()) {
                File sqlFile = new File(f, "README.md");
                list.addAll(walk(f.getAbsolutePath()));

                final ResourceType jdbcResourceType;
                switch (f.getName()) {
                    case "schema":
                        jdbcResourceType = new SchemaJdbcResourceType(dataSource);
                        break;
                    case "table":
                        jdbcResourceType = new TableJdbcResourceType(dataSource);
                        break;
                    case "child":
                        jdbcResourceType = new ChildTableJdbcResourceType(dataSource);
                        break;
                    case "parent":
                        jdbcResourceType = new ParentTableJdbcResourceType(dataSource);
                        break;
                    case "pk":
                        jdbcResourceType = new PkJdbcResourceType(dataSource);
                        break;
                    case "index":
                        jdbcResourceType = new IndexJdbcResourceType(dataSource);
                        break;
                    case "fk":
                        jdbcResourceType = new FkJdbcResourceType(dataSource);
                        break;
                    case "view":
                        jdbcResourceType = new ViewJdbcResourceType(dataSource);
                        break;
                    case "column":
                        jdbcResourceType = new ColumnJdbcResourceType(dataSource);
                        break;
                    case "function":
                        jdbcResourceType = new FunctionJdbcResourceType(dataSource);
                        break;
                    case "procedure":
                        jdbcResourceType = new ProcedureJdbcResourceType(dataSource);
                        break;
                    default:
                        jdbcResourceType = null;
                }

                String sql = null;
                try {
                    MarkdownFile markdownFile = new MarkdownFile(readFileToString(sqlFile, UTF_8));
                    Map<String, String> parse = markdownFile.parse();
                    Iterator<Map.Entry<String, String>> iterator = parse.entrySet().iterator();
                    if (iterator.hasNext()) {
                        sql = iterator.next().getValue();
                    }
                } catch (IOException e) {
                    // TODO catch and process this exception
                }


                final ResourceType baseResourceType;
                if (sqlFile.exists() && sql != null) {
                    baseResourceType = new SqlResourceType(new JdbcSqlQuery(dataSource, new GroovyTemplateGenerator(sql)), singletonList(f.getName()));
                } else if (jdbcResourceType != null) {
                    baseResourceType = jdbcResourceType;
                } else {
                    baseResourceType = null;
                }
                final ResourceType resourceType = new SelectWrapper(
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
    public String name() {
        throw new BootException("Not implemented!");
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
