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

package com.github.mgramin.sqlboot.model.uri

import com.fasterxml.jackson.annotation.JsonProperty
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
     * @return
     */
    fun recursive(): Boolean

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

}
