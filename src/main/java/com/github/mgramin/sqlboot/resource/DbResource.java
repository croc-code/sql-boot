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

package com.github.mgramin.sqlboot.resource;

import com.github.mgramin.sqlboot.resource_type.ResourceType;
import com.github.mgramin.sqlboot.uri.Uri;
import java.util.Map;

/**
 * DB resource
 * e.g. table "PERSONS", index "PERSONS_NAME_IDX",
 * stored function "GET_ALL_DEPARTMENTS()" etc
 */
public interface DbResource {

    /**
     * Name of db resource, e.g. "PERSONS", "JOBS", "GET_ALL_SALARY" etc.
     *
     * @return Name
     */
    String name();

    /**
     * Type of db resource, e.g. "table", "index", "stored function" etc.
     *
     * @return Type
     */
    ResourceType type();

    /**
     * URI of db resource, e.g. table/hr.persons, idx/hr.jobs_pk_idx/drop etc.
     *
     * @return URI
     */
    Uri dbUri();

    /**
     * Headers of db resource.
     *
     * @return Headers
     */
    Map<String, String> headers();

    /**
     * Body of db resource, e.g. ddl-code, html-representation, xml, json etc.
     *
     * @return Body
     */
    String body();

}
