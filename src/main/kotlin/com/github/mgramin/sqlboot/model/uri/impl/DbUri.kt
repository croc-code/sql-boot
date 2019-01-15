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
import java.util.Arrays.asList
import java.util.LinkedHashMap

/**
 * Created by maksim on 12.06.16.
 */
class DbUri : Uri {

    private val type: String
    private val action: String
    private val objects: List<String>
    private val recursive: Boolean?
    private val params = LinkedHashMap<String, String>()

/*
    constructor(type: String, vararg objects: String) {
        this.type = type
        this.objects = Arrays.asList(*objects)
        this.action = ""
        this.recursive = false
    }
*/

    constructor(type: String, objects: List<String>) {
        this.type = type
        this.objects = objects
        this.action = ""
        this.recursive = false
    }

    @Throws(BootException::class)
    constructor(uriString: String) {
        try {
            val uri = URI(uriString)
            val pathString = uri.path.replace("*", "%")
            val queryString = uri.query

            val list = asList(*pathString.split("[/]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            type = list[0]
            if (list.size == 1) {
                objects = asList("%")
            } else {
                objects = asList(*list[1].split("[.]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            }
            if (list.size == 3) {
                action = list[2]
            } else {
                action = ""
            }
            recursive = pathString[pathString.length - 1] == '/'

            if (queryString != null)
                for (s in queryString.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                    params[s.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]] = s.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                }
        } catch (e: Exception) {
            throw BootException(e)
        }
    }

    override fun type(): String {
        return type
    }

    override fun path(): List<String> {
        return objects
    }

    override fun path(index: Int): String {
        return if (index > objects.size - 1) {
            "%"
        } else {
            objects[index]
        }
    }

    override fun recursive(): Boolean? {
        return recursive
    }

    override fun params(): Map<String, String> {
        return params
    }

    override fun action(): String {
        return this.action
    }

    override fun toString(): String {
        val result = StringBuilder(type + "/" + objects.joinToString("."))
        if (action != "" && action != "create")
            result.append("/").append(action)
        if (recursive != null && recursive)
            result.append("/")
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