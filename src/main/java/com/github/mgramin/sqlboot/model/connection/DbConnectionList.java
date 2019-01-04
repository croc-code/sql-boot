/*
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2019 Maksim Gramin
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

import static java.lang.String.format;

import com.github.mgramin.sqlboot.exceptions.BootException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@Service
@Configuration
@ConfigurationProperties(prefix = "conf")
public class DbConnectionList {

    private List<DbConnection> connections = new ArrayList<>();

    public List<DbConnection> getConnections() {
        return connections;
    }

    public void setConnections(final List<DbConnection> connections) {
        this.connections = connections;
    }

    public DbConnection getDefaultConnection() {
        return connections.stream()
            .findFirst()
            .orElseThrow(() ->
                new BootException("Default connection not found."));
    }

    public DbConnection getConnectionByName(final String name) {
        if (name == null) {
            return getDefaultConnection();
        }
        return connections.stream()
            .filter(v -> v.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new BootException(
                format("Connection with name <<%s>> not found.", name)));
    }


    public List<DbConnection> getConnectionsByMask(final String name) {
        if (name == null) {
            return Collections.singletonList(getDefaultConnection());
        }
        return connections.stream()
            .filter(v -> v.getName().matches(name))
            .collect(Collectors.toList());
    }

}
