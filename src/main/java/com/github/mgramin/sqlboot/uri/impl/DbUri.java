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

package com.github.mgramin.sqlboot.uri.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.uri.Uri;
import static java.util.Arrays.asList;

/**
 * Created by maksim on 12.06.16.
 */
// TODO create and use interface
public final class DbUri implements Uri {

    private final String type;
    private final String action;
    private final List<String> objects;
    private final Boolean recursive;
    private final Map<String, String> params = new LinkedHashMap<>();
    private final Map<String, String> filters = new HashMap<>();

    public DbUri(final String type, final List<String> objects) {
        this.type = type;
        this.objects = objects;
        this.action = null;
        this.recursive = false;
    }

    public DbUri(final String uriString) throws BootException {
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
            if (list.size() == 3) {
                action = list.get(2);
            } else {
                action = null;
            }
            recursive = pathString.charAt(pathString.length() - 1) == '/';

            if (queryString != null)
                for (String s : queryString.split("&")) {
                    params.put(s.split("=")[0], s.split("=")[1]);
            }
        } catch (Exception e) {
            throw new BootException(e);
        }
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String action() {
        return action;
    }

    @Override
    public List<String> objects() {
        return objects;
    }

    @Override
    @Deprecated // TODO only for Queryable uri
    public Boolean recursive() {
        return recursive;
    }

    @Override
    public Map<String, String> params() {
        return params;
    }

    @Override
    @Deprecated // ?
    public Map<String, String> filters() {
        return filters;
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

    @Override
    public String toJson() {
        String s = "DbUri{" +
            "type='" + type + '\'' +
            ", dbSchemaObjectCommand='" + action + '\'' +
            ", objects=" + objects +
            ", recursive=" + recursive +
            ", params=" + params +
            "}";
        s = s.replace("%", "*");
        return s;
    }

}