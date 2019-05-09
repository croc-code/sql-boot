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

package com.github.mgramin.sqlboot.model.uri.impl

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.model.uri.Uri
import java.net.URI

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
            val uri = URI(uriString)

            val pathMap: Map<Int, String> = uri.path
                    .split("/")
                    .filter { it.isNotEmpty() }
                    .mapIndexed { index, element -> index to element }
                    .toMap()

            connection = pathMap.getValue(0)
            type = pathMap.getValue(1)
            objects = pathMap.getOrDefault(2, "*").split(".")
            action = pathMap.getOrDefault(3, "")

            params = if (uri.query != null)
                uri.query
                        .split("&")
                        .map { it.split("=")[0] to it.split("=")[1] }
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