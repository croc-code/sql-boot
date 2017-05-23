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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.script.aggregators.wrappers;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.script.aggregators.Aggregator;

import java.util.List;
import java.util.Map;

/**
 * Created by maksim on 21.05.17.
 */
public class HttpAggregatorWrapper implements Aggregator{

    private final Aggregator origin;
    private final Map<String, String> requestHeaders;
    private final Map<String, String> responseHeaders;

    public HttpAggregatorWrapper(Aggregator origin, Map<String, String> requestHeaders, Map<String, String> responseHeaders) {
        this.origin = origin;
        this.requestHeaders = requestHeaders;
        this.responseHeaders = responseHeaders;
    }

    public Map<String, String> requestHeaders() {
        return this.requestHeaders;
    }

    public Map<String, String> responseHeaders() {
        return this.responseHeaders;
    }

    @Override
    public String name() {
        return this.origin.name();
    }

    @Override
    public Boolean isDefault() {
        return this.origin.isDefault();
    }

    @Override
    public byte[] aggregate(List<DbResource> objects) throws SqlBootException {
        return origin.aggregate(objects);
    }

}
