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

package com.github.mgramin.sqlboot.model.resourcetype.impl

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.model.connection.SimpleDbConnection
import com.github.mgramin.sqlboot.model.dialect.Dialect
import com.github.mgramin.sqlboot.model.resourcetype.Metadata
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.body.BodyWrapper
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.header.SelectWrapper
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.list.SortWrapper
import com.github.mgramin.sqlboot.model.uri.Uri
import com.github.mgramin.sqlboot.template.generator.impl.GroovyTemplateGenerator
import com.github.mgramin.sqlboot.tools.files.file.impl.MarkdownFile
import com.github.mgramin.sqlboot.tools.files.file.impl.SimpleFile
import reactor.core.publisher.Flux
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8

/**
 * Created by MGramin on 11.07.2017.
 */
class FsResourceType(
        private val dbConnections: List<SimpleDbConnection>,
        private val dialects: List<Dialect>
) : ResourceType {

    private val resourceTypes: List<ResourceType> = walk(dbConnections.first().baseFolder!!.file.path)

    private fun walk(path: String) =
            File(path)
                    .walkTopDown()
                    .filter { it.isFile }
                    .filter { it.extension.equals("md", true) || it.extension.equals("sql", true) }
                    .map { return@map if (it.extension.equals("md", true)) MarkdownFile(it.name, it.readText(UTF_8)) else SimpleFile(it.name, it.readText(UTF_8).toByteArray()) }
                    .filter { it.content().isNotEmpty() }
                    .map { createObjectType(it) }
                    .toList()

    private fun createObjectType(it: com.github.mgramin.sqlboot.tools.files.file.File) =
            SelectWrapper(
                    SortWrapper(
                            BodyWrapper(
                                    SqlResourceType(
                                            aliases = listOf(File(it.name()).nameWithoutExtension),
                                            sql = it.content().toString(Charset.defaultCharset()),
                                            connections = dbConnections,
                                            dialects = dialects),
                                    templateGenerator = GroovyTemplateGenerator("[EMPTY BODY]"))))

    @Deprecated("")
    fun resourceTypes() = resourceTypes

    override fun aliases() = throw BootException("Not implemented!")

    override fun path() = throw BootException("Not implemented!")

    override fun read(uri: Uri) =
            Flux.merge(
                    resourceTypes
                            .filter { it.name().matches(wildcardToRegex(uri)) }
                            .map { it.read(uri) })

    override fun metaData(uri: Uri): List<Metadata> =
            resourceTypes
                    .filter { it.name().matches(wildcardToRegex(uri)) }
                    .flatMap { it.metaData(uri) }

    private fun wildcardToRegex(uri: Uri) = uri.type().replace("?", ".?").replace("*", ".*?").toRegex()

}
