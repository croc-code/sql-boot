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

import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import java.util.Map;
import com.github.mgramin.sqlboot.exceptions.BootException;

/**
 * DB resource without body
 */
//@ToString
public final class DbResourceThin implements DbResource {

    private final String name;
    private final ResourceType type;
    private final Uri uri;
    private final Map<String, String> headers;

    public DbResourceThin(final String name, final ResourceType type,
                          final Uri uri,
                          final Map<String, String> headers) {
        this.name = name;
        this.type = type;
        this.uri = uri;
        this.headers = headers;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ResourceType type() {
        return type;
    }

    @Override
    public Uri dbUri() {
        return uri;
    }

    @Override
    public Map<String, String> headers() {
        return headers;
    }

    @Override
    public String body() {
        throw new BootException("Resource body not allow here.");
    }

}
