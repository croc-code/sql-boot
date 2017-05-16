/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 mgramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.github.mgramin.sqlboot.model;

import lombok.ToString;

import java.util.Properties;

/**
 * DB resource without body
 */
@ToString
public class DbResourceThin implements DbResource {

    private final String name;
    private final DbResourceType type;
    private final DbUri dbUri;
    private final Properties headers;

    public DbResourceThin(String name, DbResourceType type, DbUri dbUri, Properties headers) {
        this.name = name;
        this.type = type;
        this.dbUri = dbUri;
        this.headers = headers;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public DbResourceType type() {
        return type;
    }

    @Override
    public DbUri dbUri() {
        return dbUri;
    }

    @Override
    public Properties headers() {
        return headers;
    }

    @Override
    public String body() {
        return name + " [EMPTY BODY]";
//        throw new SqlBootException("Resource body not allow here.");
    }

}