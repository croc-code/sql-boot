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

package com.github.mgramin.sqlboot.model.resourcetype.impl

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.model.connection.Endpoint
import com.github.mgramin.sqlboot.model.dialect.Dialect
import com.github.mgramin.sqlboot.model.resource.DbResource
import com.github.mgramin.sqlboot.model.resourcetype.Metadata
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.body.BodyWrapper
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.header.SelectWrapper
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.header.TypeWrapper
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.list.SortWrapper
import com.github.mgramin.sqlboot.model.uri.Uri
import com.github.mgramin.sqlboot.template.generator.impl.FakeTemplateGenerator
import com.github.mgramin.sqlboot.tools.files.file.impl.MarkdownFile
import com.github.mgramin.sqlboot.tools.files.file.impl.SimpleFile
import org.springframework.core.io.FileSystemResource
import reactor.core.publisher.Flux
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

/**
 * Created by MGramin on 11.07.2017.
 */
class FsResourceType(
        private val endpoints: List<Endpoint>,
        private val dialects: List<Dialect>
) : ResourceType {

    private val resourceTypes: List<ResourceType> =
            File(FileSystemResource(endpoints.first().confDir()).file.path)
                    .walkTopDown()
                    .filter { it.isFile }
                    .filter { it.extension.equals("sql", true) }
                    .map {
                        if (it.extension.equals("md", true))
                            MarkdownFile(it.name, it.readText(UTF_8))
                        else SimpleFile(it.name, listOf(it.readText(UTF_8)))
                    }
                    .filter { it.content().isNotEmpty() }
                    .flatMap { it.content().asSequence() }
                    .map { createObjectType(it) }
                    .toList()

    override fun aliases() = throw BootException("Not implemented!")

    override fun path() = throw BootException("Not implemented!")

    override fun read(uri: Uri): Flux<DbResource> =
            Flux.merge(
                    resourceTypes
                            .filter { it.name().matches(wildcardToRegex(uri)) }
                            .map { it.read(uri) })

    override fun metaData(uri: Uri): List<Metadata> =
            resourceTypes
                    .filter { it.name().matches(wildcardToRegex(uri)) }
                    .flatMap { it.metaData(uri) }

    override fun toJson() = throw BootException("Not implemented!")

    @Deprecated("")
    fun resourceTypes() = resourceTypes

    private fun createObjectType(it: String) =
            TypeWrapper(
                    SelectWrapper(
                            SortWrapper(
                                    BodyWrapper(
                                            SqlResourceType(
                                                    sql = it,
                                                    endpoints = endpoints,
                                                    dialects = dialects),
                                            templateGenerator = FakeTemplateGenerator("[EMPTY BODY]")))))

    private fun wildcardToRegex(uri: Uri) =
            uri.type().replace("?", ".?").replace("*", ".*?").toRegex()

}
