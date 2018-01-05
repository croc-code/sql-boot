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

package com.github.mgramin.sqlboot.model.resource_type;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.uri.Uri;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 *  Resource type
 */
public interface ResourceType {

    /**
     * Name of resource type, e.g "table", "index", "stored prcedure" etc
     *
     * @return
     */
    @JsonProperty
    String name();

    /**
     * Aliases of resource type, e.g. "table", "tbl", "t"
     *
     * @return
     */
    List<String> aliases();

    /**
     * Path of resource
     * e.g. "schema -> table -> column"
     * or "schema -> table -> index -> index_column"
     * @return
     */
    List<String> path();

    /**
     * Retrieves a map that contains information about the resource properties
     * "name" -> "type"
     *
     * @return
     */
    Map<String, String> metaData();

    /**
     * Read resources by uri
     *
     * @param uri
     * @return
     * @throws BootException
     */
    Stream<DbResource> read(Uri uri) throws BootException;

}
