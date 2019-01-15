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

package com.github.mgramin.sqlboot.model.resourcetype.wrappers.list

import com.github.mgramin.sqlboot.model.resource.DbResource
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.uri.Uri
import org.apache.commons.lang3.StringUtils.substringAfter
import org.apache.commons.lang3.StringUtils.substringBefore
import java.lang.Integer.valueOf

class PageWrapper constructor(
    private val origin: ResourceType,
    private val parameterName: String = "page",
    private val pageSize: Int = 10,
    private val delimiter: String = ","
) : ResourceType {

    override fun aliases(): List<String> {
        return origin.aliases()
    }

    override fun path(): List<String> {
        return origin.path()
    }

    override fun metaData(): Map<String, String> {
        return origin.metaData()
    }

    override fun read(uri: Uri): Sequence<DbResource> {
        val pageParameter = uri.params()[parameterName]
        if (pageParameter != null) {
            val pageNumber = valueOf(substringBefore(pageParameter, delimiter))
            val pageSize: Int
            if (substringAfter(pageParameter, delimiter).isEmpty()) {
                pageSize = this.pageSize
            } else {
                pageSize = valueOf(substringAfter(pageParameter, delimiter))
            }
            return origin.read(uri).drop(((pageNumber - 1) * pageSize)).take(pageSize)
        } else {
            return origin.read(uri)
        }
    }
}
