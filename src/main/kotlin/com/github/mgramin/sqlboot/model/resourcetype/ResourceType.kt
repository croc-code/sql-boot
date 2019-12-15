/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2019, CROC Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.mgramin.sqlboot.model.resourcetype

import com.fasterxml.jackson.databind.JsonNode
import com.github.mgramin.sqlboot.model.resource.DbResource
import com.github.mgramin.sqlboot.model.uri.Uri
import reactor.core.publisher.Flux
import java.io.Serializable

/**
 * Resource type e.g. Table, Index, Stored function etc
 */
interface ResourceType : Serializable {

    /**
     * Name of resource type, e.g "table", "index", "stored procedure" etc
     */
    fun name() = aliases()[0]

    /**
     * Aliases of resource type, e.g. ["table", "tbl", "t", "tablo"]
     */
    fun aliases(): List<String>

    /**
     * Path of resource e.g. ["schema", "table", "column"] or ["schema", "table", "index", "index_column"]
     */
    fun path(): List<String>

    /**
     * Information about the resource metadata (properties) "name" -> "type"
     * TODO rename to columns(uri) ?
     */
    fun metaData(uri: Uri): List<Metadata>

    /**
     * Read resources by uri
     */
    fun read(uri: Uri): Flux<DbResource>

    /**
     * Get as JSON
     */
    fun toJson(): JsonNode

}
