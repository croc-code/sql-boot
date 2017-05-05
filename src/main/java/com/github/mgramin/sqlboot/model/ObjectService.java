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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by maksim on 10.07.16.
 */
@Deprecated
public class ObjectService {

    Map<String, DbResource> objects;
    private String baseURI;

    public ObjectService(Map<String, DbResource> objects, String baseURI) {
        this.objects = objects;
        this.baseURI = baseURI;
    }

    public List<DbResource> get(String type) {
        List<DbResource> result = new ArrayList<>();
        for (Map.Entry<String, DbResource> entry : objects.entrySet()) {
            if (entry.getKey().startsWith(type + "/" + baseURI + ".")) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    public Integer getMaxLength(String type, String name) {
        DbResource column = get(type).stream()/*.filter(o -> o.paths.get("table").equalsIgnoreCase(name))*/.max(Comparator.comparing(i -> i.paths().get(type))).get();
        return column.paths().get("column").length() + 7;
    }

}
