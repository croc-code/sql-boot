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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.ColumnJdbcResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.SchemaJdbcResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.jdbc.TableJdbcResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.sql.SqlResourceType;
import com.github.mgramin.sqlboot.model.resource_type.wrappers.body.SqlBodyWrapper;
import com.github.mgramin.sqlboot.model.resource_type.wrappers.body.TemplateBodyWrapper;
import com.github.mgramin.sqlboot.model.resource_type.wrappers.header.SelectWrapper;
import com.github.mgramin.sqlboot.model.resource_type.wrappers.list.LimitWrapper;
import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.sql.impl.JdbcSqlQuery;
import com.github.mgramin.sqlboot.template.generator.impl.GroovyTemplateGenerator;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.lang3.StringUtils.substringBetween;

/**
 * Created by MGramin on 11.07.2017.
 */
@Service
@Configuration
@ConfigurationProperties(prefix = "conf")
public class FsResourceTypes implements ResourceType {

    private List<DbConnection> connections = new ArrayList<>();
    private List<ResourceType> resourceTypes = new ArrayList<>();

    private DbConnection getConnectionByName(String name) {
        return connections.stream()
            .filter(v -> v.name.equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    private DbConnection getDefaultConnection() {
        return connections.stream()
            .findFirst()
            .orElse(null);
    }

    public List<DbConnection> getConnections() {
        return connections;
    }

    public void setConnections(
        List<DbConnection> connections) {
        this.connections = connections;
    }

    public static class DbConnection {

        private String name;
        private Resource baseFolder;
        private String url;
        private String user;
        private String password;
        private String driverClassName;

        private DataSource dataSource;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JsonIgnore
        public Resource getBaseFolder() {
            return baseFolder;
        }

        public String getConfigurationFolder() throws IOException {
            return baseFolder.getFile().getPath();
        }

        public void setBaseFolder(Resource baseFolder) {
            this.baseFolder = baseFolder;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        @JsonIgnore
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        @JsonIgnore
        public DataSource getDataSource() {
            return dataSource;
        }

        public void setDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
        }
    }

    public FsResourceTypes() {
    }

    public void init(String connectionName) throws BootException {
        this.connections.forEach(v -> {
            final DataSource ds = new DataSource();
            ofNullable(v.getDriverClassName()).ifPresent(ds::setDriverClassName);
            ofNullable(v.getUrl()).ifPresent(ds::setUrl);
            ofNullable(v.getUser()).ifPresent(ds::setUsername);
            ofNullable(v.getPassword()).ifPresent(ds::setPassword);
            v.setDataSource(ds);
        });

        final DbConnection dbConnection;
        if (connectionName == null) {
            dbConnection = getDefaultConnection();
        } else {
            dbConnection = getConnectionByName(connectionName);
        }

        resourceTypes.clear();
        try {
            final String baseFolder = dbConnection.getBaseFolder().getFile().getPath();
            final DataSource dataSource = dbConnection.getDataSource();
            walk(baseFolder, dataSource);
        } catch (IOException e) {
            throw new BootException(e);
        }
    }

    @Deprecated
    public ResourceType type(final String name) {
        return resourceTypes.stream().filter(v -> v.name().equalsIgnoreCase(name)).findAny().get();
    }

    @Deprecated
    public List<ResourceType> resourceTypes() {
        return resourceTypes;
    }

    private List<ResourceType> walk(final String path, final DataSource dataSource) {
        File[] files = new File(path).listFiles();
        if (files == null) return null;
        List<ResourceType> list = new ArrayList<>();
        for (File f : files) {
            if (f.isDirectory()) {
                File sqlFile = new File(f, "README.md");
                List<ResourceType> child = walk(f.getAbsolutePath(), dataSource);

                final ResourceType jdbcResourceType;
                switch (f.getName()) {
                    case "schema":
                        jdbcResourceType = new SchemaJdbcResourceType(dataSource);
                        break;
                    case "table":
                        jdbcResourceType = new TableJdbcResourceType(dataSource);
                        break;
                    case "column":
                        jdbcResourceType = new ColumnJdbcResourceType(dataSource);
                        break;
                    default:
                        jdbcResourceType = null;
                }

                String sql = null;
                try {
                    sql = substringBetween(readFileToString(sqlFile, UTF_8), "````sql", "````");
                } catch (IOException e) {
                    // TODO catch process this exception
                }

                String ddlSql = null;
                try {
                    ddlSql = substringBetween(readFileToString(sqlFile, UTF_8), "```sql-template", "```");
                } catch (IOException e) {
                    // TODO catch process this exception
                }
                if (ddlSql == null) {
                    ddlSql = "";
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
                    new SqlBodyWrapper(
                        new TemplateBodyWrapper(
                            new LimitWrapper(
//                                new WhereWrapper(
                                    baseResourceType),
                            new GroovyTemplateGenerator(ddlSql)),
                        dataSource));
                if (baseResourceType != null) {
                    resourceTypes.add(resourceType);
                    list.add(resourceType);
                }
            }
        }
        return list;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public Stream<DbResource> read(Uri uri) throws BootException {
        ResourceType type = type(uri.type());
        return type.read(uri);
    }

    @Override
    public Map<String, String> medataData() {
        throw new BootException("Not implemented!");
    }

}
