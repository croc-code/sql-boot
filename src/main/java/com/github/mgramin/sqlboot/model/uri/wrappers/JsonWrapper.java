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

package com.github.mgramin.sqlboot.model.uri.wrappers;

import com.github.mgramin.sqlboot.model.uri.Uri;
import java.util.List;
import java.util.Map;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class JsonWrapper implements Uri {

    private final Uri origin;

    public JsonWrapper(Uri origin) {
        this.origin = origin;
    }

    @Override
    public String type() {
        return origin.type();
    }

    @Override
    public List<String> path() {
        return origin.path();
    }

    @Override
    public String path(Integer index) {
        return origin.path(index);
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
    public String action() {
        return origin.action();
    }

    @Override
    public String toString() {
        String s = "DbUri{" +
            "type='" + origin.type() + '\'' +
            ", path=" + origin.path() +
            ", recursive=" + origin.recursive() +
            ", params=" + origin.params() +
            "}";
        s = s.replace("%", "*");
        return s;
    }


}
