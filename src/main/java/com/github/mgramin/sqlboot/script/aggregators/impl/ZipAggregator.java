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
import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.script.aggregators.AbstractAggregator;
import com.github.mgramin.sqlboot.script.aggregators.Aggregator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.mgramin.sqlboot.util.ZipHelper.compress;

/**
 * Created by mgramin on 17.12.2016.
 */
public final class ZipAggregator extends AbstractAggregator implements Aggregator {

    public ZipAggregator(String name) {
        this.name = name;
    }

    @Override
    public byte[] aggregate(List<DbResource> objects) throws SqlBootException {
        Map<String, byte[]> files = new HashMap<>();
        for (DbResource o : objects) {
            if (o.headers().getProperty("file_name") != null && !o.headers().getProperty("file_name").isEmpty())
                files.put(o.headers().getProperty("file_name").toLowerCase(), o.body().getBytes());
        }
        return compress(files);
    }

}
