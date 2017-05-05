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

import java.util.Map;
import java.util.Properties;

/**
 * DB Resource
 * e.g. table "PERSONS", index "PERSONS_NAME_IDX", stored function "GET_ALL_DEPARTMENTS()" etc
 */
@ToString
public class DbResource implements Comparable<DbResource> {

    public String name;
    public DBResourceType type;
    public ObjUri objUri;

    public Properties headers = new Properties();
    public String body;

    @Deprecated
    public Map<String, String> paths;


    public String getProp(String key) {
        return headers.getProperty(key);
    }

    public void addProperty(Object key, Object value){
        this.headers.put(key, value);
    }

    public String getName() {
        return name;
    }

    public DBResourceType getType() {
        return type;
    }

    public String getBody() {
        return body;
    }

    public ObjUri getObjUri() {
        return objUri;
    }

    public Map<String, String> getPaths() {
        return paths;
    }

    public Properties getHeaders() {
        return headers;
    }

    @Override
    public int compareTo(DbResource o) {
        return (this.objUri.toString()).compareTo(o.objUri.toString());
    }

}