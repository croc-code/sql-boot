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

package com.github.mgramin.sqlboot.model.resource_type

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.mgramin.sqlboot.model.resource.DbResource
import com.github.mgramin.sqlboot.model.uri.Uri
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

/**
 * Resource type e.g. Table, Index, Stored function etc
 */
interface ResourceType {

    /**
     * Name of resource type, e.g "table", "index", "stored procedure" etc
     */
    @JsonProperty
    @JvmDefault
    fun name(): String {
        return aliases()[0]
    }

    /**
     * Aliases of resource type, e.g. ["table", "tbl", "t", "tablo"]
     */
    @JsonProperty
    fun aliases(): List<String>

    /**
     * Path of resource e.g. ["schema", "table", "column"] or ["schema", "table", "index",
     * "index_column"]
     */
    @JsonProperty
    fun path(): List<String>

    /**
     * Retrieves a map that contains information about the resource metadata (properties) "name" ->
     * "type"
     */
    @JsonProperty
    fun metaData(): Map<String, String>

    @JvmDefault
    fun metaData(uri: Uri): List<Metadata> {
        return metaData().entries
                .map { e -> ResourceType.Metadata(e.key, e.value) }
                .toList()
    }

    /**
     * Read resources by uri
     */
    fun read(uri: Uri): Sequence<DbResource>

    @JsonAutoDetect(fieldVisibility = Visibility.ANY)
    class Metadata(
        private val name: String,
        private val type: String,
        private val description: String
    ) {
        private val properties: MutableMap<String, Any>

        constructor(name: String, description: String) : this(name, "String", description)

        init {
            this.properties = HashMap()
            try {
                this.properties.putAll(JSONObject(description).toMap())
                this.properties["key"] = name.replace("@", "")
            } catch (ignored: JSONException) {
                this.properties.clear()
                val prop = HashMap<String, Any>()
                prop["key"] = name.replace("@", "")
                prop["label"] = name.replace("@", "")
                prop["description"] = description
                this.properties.putAll(prop)
            }
        }

        fun name(): String {
            return name
        }

        fun description(): String {
            return description
        }

        fun properties(): Map<String, Any> {
            return properties
        }
    }
}
