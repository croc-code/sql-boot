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

package com.github.mgramin.sqlboot.model.connection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mgramin.sqlboot.sql.select.impl.JdbcSelectQuery;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.core.io.Resource;

import static java.util.Optional.ofNullable;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class DbConnection {

    private String name;
    @JsonIgnore
    private Resource baseFolder;
    private String url;
    private String user;
    @JsonIgnore
    private String password;
    private String driverClassName;

    private DataSource dataSource;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Resource getBaseFolder() {
        return baseFolder;
    }

    public void setBaseFolder(Resource baseFolder) {
        this.baseFolder = baseFolder;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getHealth() {
        try {
            new JdbcSelectQuery(getDataSource(), "").dbHealth();
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @JsonIgnore
    public DataSource getDataSource() {
        if (dataSource != null) {
            return dataSource;
        } else {
            dataSource = new DataSource();
            ofNullable(driverClassName).ifPresent(dataSource::setDriverClassName);
            ofNullable(url).ifPresent(dataSource::setUrl);
            ofNullable(user).ifPresent(dataSource::setUsername);
            ofNullable(password).ifPresent(dataSource::setPassword);
            return dataSource;
        }
    }

}
