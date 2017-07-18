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

package com.github.mgramin.sqlboot.readers.wrappers;

import java.util.List;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.Uri;
import com.github.mgramin.sqlboot.readers.DbResourceReader;
import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;

/**
 * Created by maksim on 22.05.17.
 */
public final class LimitWrapper implements DbResourceReader {

    private final DbResourceReader origin;

    /**
     * Ctor.
     *
     * @param origin
     */
    public LimitWrapper(final DbResourceReader origin) {
        this.origin = origin;
    }

    @Override
    public List<DbResource> read(final Uri uri, final ResourceType type) throws BootException {
        final List<DbResource> resources = origin.read(uri, type);
        final String limit = uri.params().get("limit");
        return ofNullable(limit)
                .map(v -> resources.subList(0, parseInt(v)))
                .orElse(resources);
    }

}
