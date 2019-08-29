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

package com.github.mgramin.sqlboot.model.uri

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.apache.commons.lang3.StringUtils
import java.io.Serializable

/**
 * Resource URI.
 *
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: d86c819560d694fecf8e1ca3c9e80e1d666d8f93 $
 * @since 0.1
 */
interface Uri : Serializable {

    /**
     * Type.
     *
     * @return Type name.
     */
    @JsonProperty
    fun type(): String

    /**
     *
     *
     * @return
     */
    @JsonProperty
    fun path(): List<String>

    /**
     *
     * @param index
     * @return
     */
    fun path(index: Int): String


    /**
     *
     *
     * @return
     */
    fun params(): Map<String, String>

    /**
     *
     * @return
     */
    fun action(): String

    /**
     * Connection name
     */
    fun connection(): String


    fun pageNumber(): Int {
        val pageParameter = params()["page"]
        val pageNumber: Int
        pageNumber = if (pageParameter != null) {
            val delimiter = ","
            Integer.valueOf(StringUtils.substringBefore(pageParameter, delimiter))
        } else {
            1
        }
        return pageNumber
    }

    fun pageSize(): Int {
        val pageParameter = params()["page"]
        return if (pageParameter != null) {
            val delimiter = ","
            if (StringUtils.substringAfter(pageParameter, delimiter).isEmpty()) {
                10
            } else {
                Integer.valueOf(StringUtils.substringAfter(pageParameter, delimiter))
            }
        } else {
            10
        }
    }

    fun orderedColumns(): Map<String, String> {
        return (params()["orderby"] ?: "")
                .split(",")
                .asSequence()
                .filter { it.isNotBlank() }
                .map { return@map if (!it.contains("-")) "$it-asc" else it }
                .map { it.substringBefore("-") to it.substringAfter("-") }
                .toMap()
    }

    fun filter(): JsonObject {
        return if (params().containsKey("filter")) {
            JsonParser().parse(params()["filter"]).asJsonObject
        } else {
            JsonObject()
        }
    }

}
