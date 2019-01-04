/*
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2019 Maksim Gramin
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.model.resource_type.wrappers.list;

import static java.lang.Integer.valueOf;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.uri.Uri;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PageWrapper implements ResourceType {

    private final ResourceType origin;
    private final String page;
    private final int page_size;

    public PageWrapper(ResourceType origin) {
        this(origin, "page", 10);
    }

    public PageWrapper(ResourceType origin, String page, int page_size) {
        this.page_size = page_size;
        this.page = page;
        this.origin = origin;
    }

    @Override
    public List<String> aliases() {
        return origin.aliases();
    }

    @Override
    public List<String> path() {
        return origin.path();
    }

    @Override
    public Map<String, String> metaData() {
        return origin.metaData();
    }

    @Override
    public Stream<DbResource> read(Uri uri) throws BootException {
        final String pageParameter = uri.params().get(page);
        if (pageParameter != null) {
            final Integer pageNumber = valueOf(substringBefore(pageParameter, ":"));
            final Integer pageSize;
            if (substringAfter(pageParameter, ":").equals("")) {
                pageSize = page_size;
            } else {
                pageSize = valueOf(substringAfter(pageParameter, ":"));
            }
            return origin.read(uri).skip((pageNumber - 1) * pageSize).limit(pageSize);
        } else {
            return origin.read(uri);
        }
    }

}
