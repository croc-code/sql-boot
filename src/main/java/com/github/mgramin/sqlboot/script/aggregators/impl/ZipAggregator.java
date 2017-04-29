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

package com.github.mgramin.sqlboot.script.aggregators.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBResource;
import com.github.mgramin.sqlboot.script.aggregators.AbstractAggregator;
import com.github.mgramin.sqlboot.script.aggregators.IAggregator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.mgramin.sqlboot.util.ZipHelper.compress;

/**
 * Created by mgramin on 17.12.2016.
 */
public class ZipAggregator extends AbstractAggregator implements IAggregator {

    public ZipAggregator(String name, Map<String, String> httpHeaders) {
        this.name = name;
        this.httpHeaders = httpHeaders;
    }

    @Override
    public byte[] aggregate(List<DBResource> objects) throws SqlBootException {
        Map<String, byte[]> files = new HashMap<>();
        for (DBResource o : objects) {
            if (o.getProp("file_name") != null && !o.getProp("file_name").isEmpty())
                files.put(o.getProp("file_name").toLowerCase(), o.body.getBytes());
        }
        return compress(files);
    }

}
