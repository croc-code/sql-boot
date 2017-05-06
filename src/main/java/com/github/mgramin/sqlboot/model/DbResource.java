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

import com.github.mgramin.sqlboot.uri.ObjUri;
import lombok.ToString;

import java.util.Properties;

/**
 * DB Resource
 * e.g. table "PERSONS", index "PERSONS_NAME_IDX", stored function "GET_ALL_DEPARTMENTS()" etc
 */
@ToString
public class DbResource {

    final private String name;
    final private DbResourceType type;
    final private ObjUri objUri;
    final private Properties headers;
    @Deprecated
    public String body; // TODO make final and private

    public DbResource(String name, DbResourceType type, ObjUri objUri, Properties headers) {
        this.name = name;
        this.type = type;
        this.objUri = objUri;
        this.headers = headers;
    }

    public String name() {
        return name;
    }

     public DbResourceType type() {
        return type;
    }

    public ObjUri objUri() {
        return objUri;
    }

    public Properties headers() {
        return headers;
    }

    public String headerByKey(String key) {
        return headers.getProperty(key);
    }

}