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

package com.github.mgramin.sqlboot.readers.wrappers;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.model.DbResourceType;
import com.github.mgramin.sqlboot.model.DbUri;
import com.github.mgramin.sqlboot.readers.DbResourceReader;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created by maksim on 09.05.17.
 */
public final class FilterWrapper implements DbResourceReader {

    private final DbResourceReader origin;

    public FilterWrapper(DbResourceReader origin) {
        this.origin = origin;
    }

    @Override
    public List<DbResource> read(DbUri dbUri, DbResourceType type) throws SqlBootException {
        List<DbResource> objects = origin.read(dbUri, type);
        // TODO difficult logic
        if (dbUri.params() != null) {
            Map<String, String> filtersParam = dbUri.params().entrySet().stream().filter(p ->
                !p.getKey().equalsIgnoreCase("type"))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

            for (Map.Entry<String, String> param : filtersParam.entrySet()) {
                if (param.getKey().startsWith("@")) {
                    objects = objects.stream().filter(
                    o -> o.headers().getProperty(param.getKey().substring(1))
                        .contains(param.getValue()))
                    .collect(toList());
                }
            }
        }
        return objects;
    }

}
