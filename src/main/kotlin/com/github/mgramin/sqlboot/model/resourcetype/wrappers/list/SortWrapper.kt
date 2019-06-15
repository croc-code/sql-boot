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

package com.github.mgramin.sqlboot.model.resourcetype.wrappers.list

import com.github.mgramin.sqlboot.model.resource.DbResource
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.uri.Uri
import reactor.core.publisher.Flux


class SortWrapper(private val origin: ResourceType,
                  private val parameterName: String = "orderby"
) : ResourceType {

    override fun aliases(): List<String> {
        return origin.aliases()
    }

    override fun path(): List<String> {
        return origin.path()
    }

    override fun metaData(uri: Uri) = origin.metaData(uri)

    override fun read(uri: Uri): Flux<DbResource> {
        return origin.read(uri)
        /*val read: Flux<DbResource> = origin.read(uri)
        val parameters = uri.orderedColumns()
        if (parameters.isEmpty()) {
            return read
        }
        val arrayOf: Array<(DbResource) -> Comparable<*>> = parameters
                .map { return@map { it1: DbResource -> (it1.headers()[it.key] ?: "") as Comparable<*> } }
                .toTypedArray()
        return read.sortedWith(compareByDescending(*arrayOf))*/
    }

    private fun <T> compareByDescending(vararg selectors: (T) -> Comparable<*>?): Comparator<T> {
        return Comparator { b, a -> compareValuesBy(a, b, *selectors) }
    }

    override fun toJson() = origin.toJson()
}