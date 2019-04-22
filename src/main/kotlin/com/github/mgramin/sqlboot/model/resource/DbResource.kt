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

package com.github.mgramin.sqlboot.model.resource

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.uri.Uri
import java.io.Serializable

/**
 * DB resource
 * e.g. table "PERSONS", index "PERSONS_NAME_IDX",
 * stored function "GET_ALL_DEPARTMENTS()" etc
 */
interface DbResource : Serializable {

    /**
     * Name of db resource, e.g. "PERSONS", "JOBS", "GET_ALL_SALARY" etc.
     *
     * @return Name
     */
    @JsonProperty
    fun name(): String

    /**
     * Type of db resource, e.g. "table", "index", "stored function" etc.
     *
     * @return Type
     */
    @JsonProperty
    fun type(): ResourceType

    /**
     * URI of db resource, e.g. table/hr.persons, idx/hr.jobs_pk_idx/drop etc.
     *
     * @return URI
     */
    @Deprecated("#32")
    fun dbUri(): Uri

    /**
     * Headers of db resource.
     *
     * @return Headers
     */
    @JsonProperty
    fun headers(): Map<String, Any>

    /**
     * Body of db resource, e.g. ddl-code, html-representation, xml, json etc.
     *
     * @return Body
     */
    @JsonProperty
    fun body(): String

}
