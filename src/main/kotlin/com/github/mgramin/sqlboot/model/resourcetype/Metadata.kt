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

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.util.*

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class Metadata(
        private val name: String,
        private var type: String,
        private val description: String
) : Comparable<Metadata> {

    private val properties: MutableMap<String, Any>

    constructor(name: String, description: String) : this(name, "String", description)

    init {
        this.properties = HashMap()
        try {
            val map: Map<String, Any> = Gson().fromJson(description, object : TypeToken<Map<String, Any>>() {}.type)
            this.properties.putAll(map)
            this.properties["key"] = name.replace("@", "")
            if (map.containsKey("type")) {
                this.type = map["type"].toString()
            } else {
                this.type = "String"
            }
            if (!map.containsKey("visible")) {
                this.properties["visible"] = true
            }
        } catch (ignored: Exception) {
            this.properties.clear()
            val prop = HashMap<String, Any>()
            prop["key"] = name.replace("@", "")
            prop["label"] = name.replace("@", "")
            prop["description"] = description
            prop["type"] = type
            prop["visible"] = true
            this.properties.putAll(prop)
        }
    }

    fun name(): String = name.replace("@", "")

    fun description(): String = description

    fun properties(): Map<String, Any> = properties

    /**
     * Get as JSON
     */
    fun toJson(): JsonObject {
        val jsonObject = JsonObject()
        val toJson: JsonElement = Gson().toJsonTree(properties)
        jsonObject.addProperty("name", name.replace("@", "").toLowerCase())
        jsonObject.addProperty("description", description)
        jsonObject.addProperty("value", name.replace("@", "").toLowerCase())
        jsonObject.addProperty("text", properties["label"]?.toString()?:name.replace("@", ""))
        jsonObject.add("properties", toJson)
        return jsonObject
    }

    override fun compareTo(other: Metadata): Int = if (name > other.name()) -1 else 1

}