/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2017 Maksim Gramin
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

package com.github.mgramin.sqlboot.resource_type;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.resource.DbResource;
import com.github.mgramin.sqlboot.model.IDbResourceCommand;
import com.github.mgramin.sqlboot.uri.Uri;

/**
 * Created by maksim on 16.05.17.
 */
public interface ResourceType {

    /**
     *
     * @return
     */
    @JsonProperty
    String name();

    /**
     *
     * @return
     */
    List<String> aliases();

    /**
     *
     * @param uri
     * @return
     * @throws BootException
     */
    List<DbResource> read(Uri uri) throws BootException;

    @Deprecated
    default List<ActionGenerator> generators() {
        return null;
    }

    @Deprecated
    default List<DbResource> read(Uri uri, @Deprecated IDbResourceCommand command, @Deprecated String aggregatorName) throws BootException {
        return null;
    }

}
