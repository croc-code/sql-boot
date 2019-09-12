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

package com.github.mgramin.sqlboot.model.uri.impl

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.model.uri.Uri
import org.springframework.web.util.UriComponentsBuilder


/**
 * Created by maksim on 12.06.16.
 */
class DbUri : Uri {

    private val connection: String
    private val type: String
    // TODO rename to PATH
    private val objects: List<String>
    private val params: Map<String, String>
    private val action: String

    constructor(connection: String, type: String, objects: List<String>) {
        this.connection = connection
        this.type = type
        this.objects = objects
        this.action = ""
        this.params = emptyMap()
    }

    constructor(uriString: String) {
        try {
            val uri = UriComponentsBuilder.fromUriString(uriString).build()
            val pathMap: Map<Int, String> = uri.path
                    .split("/")
                    .filter { it.isNotEmpty() }
                    .mapIndexed { index, element -> index to element }
                    .toMap()

            connection = pathMap.getValue(0)
            type = pathMap.getValue(1)

            objects = if (pathMap.containsKey(2)) {
                pathMap[2]!!.split(".")
            } else {
                emptyList()
            }

            action = pathMap.getOrDefault(3, "")

            params = if (uri.query != null)
                uri.queryParams
                        .map { it.key to it.value.first() }
                        .toMap()
            else emptyMap()

        } catch (e: Exception) {
            throw BootException(e)
        }
    }

    override fun type() = type

    override fun path() = objects

    override fun path(index: Int) =
            if (index > objects.size - 1) {
                "%"
            } else {
                objects[index]
            }

    override fun params() = params

    override fun action() = this.action

    override fun connection() = this.connection

    override fun toString(): String {
        val result = StringBuilder(connection + "/" + type + "/" + objects.joinToString("."))
        if (action != "" && action != "create")
            result.append("/").append(action)
        if (!params.isEmpty()) {
            result.append("?")
            var i = 0
            for ((key, value) in params) {
                if (++i != 1)
                    result.append("&")
                result.append(key).append("=").append(value)
            }
        }
        return result.toString().replace("%", "*")
    }

}