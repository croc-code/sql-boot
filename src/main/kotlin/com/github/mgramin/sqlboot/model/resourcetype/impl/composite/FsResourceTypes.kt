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

package com.github.mgramin.sqlboot.model.resourcetype.impl.composite

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.model.connection.SimpleDbConnection
import com.github.mgramin.sqlboot.model.resource.DbResource
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.resourcetype.impl.composite.md.MarkdownFile
import com.github.mgramin.sqlboot.model.resourcetype.impl.sql.SqlResourceType
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.body.BodyWrapper
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.header.SelectWrapper
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.list.SortWrapper
import com.github.mgramin.sqlboot.model.uri.Uri
import com.github.mgramin.sqlboot.template.generator.impl.GroovyTemplateGenerator
import reactor.core.publisher.Flux
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

/**
 * Created by MGramin on 11.07.2017.
 */
class FsResourceTypes(
        private val dbConnections: List<SimpleDbConnection>,
        private val uri: Uri
) : ResourceType {

    private val resourceTypes: List<ResourceType> = walk(dbConnections.first().baseFolder!!.file.path, uri, dbConnections.first())

    private fun walk(path: String, uri: Uri, connection: SimpleDbConnection): List<ResourceType> {
        val result = arrayListOf<ResourceType>()
        File(path)
                .listFiles()
                .asSequence()
                .filter { it.isDirectory }
                .onEach { result.addAll(walk(it.absolutePath, uri, connection)) }
                .filter { File(it, "README.md").exists() }
                .forEach { dir ->
                    val map = MarkdownFile(File(dir, "README.md").readText(UTF_8)).parse()
                    if (map.isNotEmpty()) {
                        val sql = map[uri.action()] ?: map.entries.iterator().next().value
                        val resourceType =
//                                CacheWrapper(
                                SelectWrapper(
//                                        PageWrapper(
                                        SortWrapper(
                                                BodyWrapper(
                                                        SqlResourceType(
                                                                aliases = listOf(dir.name),
                                                                sql = sql,
                                                                connections = dbConnections),
                                                        templateGenerator = GroovyTemplateGenerator("[EMPTY BODY]"))))
//                                )
                        result.add(resourceType)
                    }
                }
        return result
    }

    @Deprecated("")
    fun resourceTypes(): List<ResourceType> {
        return resourceTypes
    }

    override fun aliases(): List<String> {
        throw BootException("Not implemented!")
    }

    override fun path(): List<String> {
        throw BootException("Not implemented!")
    }

    override fun read(uri: Uri): Flux<DbResource> {
        return resourceTypes
                .asSequence()
                .first { v -> v.name().equals(uri.type(), ignoreCase = true) }
                .read(uri)
    }

    override fun metaData(): Map<String, String> {
        return resourceTypes
                .asSequence()
                .first { v -> v.name().equals(uri.type(), ignoreCase = true) }
                .metaData()
    }

}
