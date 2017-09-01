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

package com.github.mgramin.sqlboot.model.uri.wrappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.github.mgramin.sqlboot.model.uri.Uri;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class SqlPlaceholdersWrapper implements Uri {

    private final Uri origin;

    public SqlPlaceholdersWrapper(final Uri origin) {
        this.origin = origin;
    }

    @Override
    public String type() {
        return origin.type();
    }

    @Override
    public String action() {
        return origin.action();
    }

    @Override
    public List<String> path() {
        return origin.path().stream()
                .map(v -> v.replace("*", "%"))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean recursive() {
        return origin.recursive();
    }

    @Override
    public Map<String, String> params() {
        return origin.params();
    }

    @Override
    public Map<String, String> filters() {
        return origin.filters();
    }

}
