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

package com.github.mgramin.sqlboot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maksim on 10.07.16.
 */
@Deprecated
public final class ObjectService {

    private final List<DbResource> objects;
    private final String baseURI;

    public ObjectService(List<DbResource> objects, String baseURI) {
        this.objects = objects;
        this.baseURI = baseURI;
    }

    public List<DbResource> get(String type) {
        List<DbResource> result = new ArrayList<>();
        for (DbResource entry : objects) {
            if (entry.dbUri().toString().startsWith(type + "/" + baseURI + ".")) {
                result.add(entry);
            }
        }
        return result;
    }

/*
    public Integer getMaxLength(String type, String name) {
        DbResourceThin column = get(type).stream().max(Comparator.comparing(i -> i.paths().get(type))).get();
        return column.paths().get("column").length() + 7;
    }
*/

}
