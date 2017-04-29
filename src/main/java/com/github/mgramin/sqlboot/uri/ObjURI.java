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

package com.github.mgramin.sqlboot.uri;

import static java.util.Arrays.asList;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import java.net.URI;
import java.util.*;

/**
 * Created by maksim on 12.06.16.
 */
public class ObjURI {

    private String type;
    private String action;
    private List<String> objects;
    private Boolean recursive;
    private Map<String, String> params = new LinkedHashMap<>();
    private Map<String, String> filters = new HashMap<>();

    public ObjURI() {
    }

    public ObjURI(String type, List<String> objects) {
        this.type = type;
        this.objects = objects;
    }

    public ObjURI(String uriString) throws SqlBootException {
        try {
            URI uri = new URI(uriString);
            String pathString = uri.getPath().replace("*", "%");
            String queryString = uri.getQuery();

            List<String> list = asList(pathString.split("[/]"));
            type = list.get(0);
            if (list.size() == 1) {
                objects = asList("%");
            } else {
                objects = asList(list.get(1).split("[.]"));
            }
            if (list.size() == 3) action = list.get(2);
            recursive = pathString.charAt(pathString.length() - 1) == '/';

            if (queryString != null)
                for (String s : queryString.split("&")) {
                    params.put(s.split("=")[0], s.split("=")[1]);
            }
        } catch (Exception e) {
            throw new SqlBootException(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(type + "/" + String.join(".", objects));
        if (action != null && !action.equals("create"))
            result.append("/").append(action);
        if (recursive != null && recursive)
            result.append("/");
        if (!params.isEmpty()) {
            result.append("?");
            int i = 0;
            for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
                if (++i!=1)
                    result.append("&");
                result.append(stringStringEntry.getKey()).append("=").append(stringStringEntry.getValue());
            }
        }
        return result.toString().replace("%", "*");
    }

    public String toJson() {
        String s = "ObjURI{" +
            "type='" + type + '\'' +
            ", dbSchemaObjectCommand='" + action + '\'' +
            ", objects=" + objects +
            ", recursive=" + recursive +
            ", params=" + params +
            "}";
        s = s.replace("%", "*");
        return s;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getObjects() {
        return objects;
    }

    public void setObjects(List<String> objects) {
        this.objects = objects;
    }

    public Boolean getRecursive() {
        return recursive;
    }

    public void setRecursive(Boolean recursive) {
        this.recursive = recursive;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, String> filters) {
        this.filters = filters;
    }
}