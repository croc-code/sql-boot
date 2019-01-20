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

package com.github.mgramin.sqlboot.model.resourcetype.wrappers.header

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.model.resource.DbResource
import com.github.mgramin.sqlboot.model.resource.impl.DbResourceImpl
import com.github.mgramin.sqlboot.model.resource.wrappers.DbResourceBodyWrapper
import com.github.mgramin.sqlboot.model.resourcetype.Metadata
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.uri.Uri
import java.util.Arrays.asList

/**
 * Created by MGramin on 18.07.2017.
 */
class SelectWrapper(private val origin: ResourceType, private val parameterName: String = "select") : ResourceType {

    override fun aliases(): List<String> {
        return origin.aliases()
    }

    override fun path(): List<String> {
        return origin.path()
    }

    @Throws(BootException::class)
    override fun read(uri: Uri): Sequence<DbResource> {
        val select = uri.params()[parameterName]
        val resources = origin.read(uri)
        return if (select != null) {
            resources
                    .map { v ->
                        DbResourceBodyWrapper(DbResourceImpl(v.name(), v.type(), v.dbUri(),
                                v.headers()
                                        .filter { h -> asList(*select.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()).contains(h.key) }
                                        .map { it.key to it.value }
                                        .toMap()), v.body())
                    }
        } else {
            resources
        }
    }

    override fun metaData(): Map<String, String> {
        return origin.metaData()
    }

    override fun metaData(uri: Uri): List<Metadata> {
        val select = uri.params()[parameterName]
        return if (select != null) {
            origin.metaData(uri)
                    .filter { e -> asList(*select.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()).contains(e.name()) }
                    .toList()
        } else {
            origin.metaData(uri)
        }
    }
}
