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

package com.github.mgramin.sqlboot.model.resource_type.impl.composite

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.model.connection.DbConnection
import com.github.mgramin.sqlboot.model.resource.DbResource
import com.github.mgramin.sqlboot.model.resource_type.ResourceType
import com.github.mgramin.sqlboot.model.resource_type.impl.composite.md.MarkdownFile
import com.github.mgramin.sqlboot.model.resource_type.impl.sql.SqlResourceType
import com.github.mgramin.sqlboot.model.resource_type.wrappers.body.TemplateBodyWrapper
import com.github.mgramin.sqlboot.model.resource_type.wrappers.header.SelectWrapper
import com.github.mgramin.sqlboot.model.resource_type.wrappers.list.CacheWrapper
import com.github.mgramin.sqlboot.model.resource_type.wrappers.list.LimitWrapper
import com.github.mgramin.sqlboot.model.resource_type.wrappers.list.PageWrapper
import com.github.mgramin.sqlboot.model.uri.Uri
import com.github.mgramin.sqlboot.sql.select.impl.JdbcSelectQuery
import com.github.mgramin.sqlboot.template.generator.impl.GroovyTemplateGenerator
import org.apache.commons.io.FileUtils.readFileToString
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*
import java.util.stream.Stream
import javax.sql.DataSource

/**
 * Created by MGramin on 11.07.2017.
 */
class FsResourceTypes
/**
 * Ctor.
 */
@Throws(BootException::class)
constructor(dbConnection: DbConnection, uri: Uri) : ResourceType {

    /**
     *
     */
    private val dataSource: DataSource

    /**
     *
     */
    private val resourceTypes: List<ResourceType>?

    init {
        dataSource = dbConnection.getDataSource()
        try {
            val baseFolder = dbConnection.baseFolder!!.file.path
            resourceTypes = walk(baseFolder, uri)
        } catch (e: IOException) {
            throw BootException(e)
        }

    }

    @Deprecated("")
    fun resourceTypes(): List<ResourceType>? {
        return resourceTypes
    }

    /**
     *
     */
    private fun walk(path: String, uri: Uri?): List<ResourceType>? {
        val files = File(path).listFiles() ?: return null
        val list = ArrayList<ResourceType>()
        for (f in files) {
            if (f.isDirectory) {
                val sqlFile = File(f, "README.md")
                list.addAll(walk(f.absolutePath, uri)!!)

                var sql: String? = null
                try {
                    val markdownFile = MarkdownFile(
                            readFileToString(sqlFile, UTF_8))
                    val parse = markdownFile.parse()

                    if (uri != null) {
                        val s = parse[uri.action()]
                        if (s != null) {
                            sql = s
                        } else {
                            val iterator = parse.entries
                                    .iterator()
                            if (iterator.hasNext()) {
                                sql = iterator.next().value
                            }
                        }
                    } else {
                        val iterator = parse.entries
                                .iterator()
                        if (iterator.hasNext()) {
                            sql = iterator.next().value
                        }
                    }

                } catch (e: IOException) {
                    // TODO catch and process this exception
                }

                val baseResourceType: ResourceType?
                if (sqlFile.exists() && sql != null) {
                    baseResourceType = SqlResourceType(
                            JdbcSelectQuery(
                                    dataSource, GroovyTemplateGenerator(sql)),
                            listOf(f.name))
                } else {
                    baseResourceType = null
                }

                if (baseResourceType != null) {
                    val resourceType = CacheWrapper(
                            SelectWrapper(
                                    //                    new SqlBodyWrapper(
                                    TemplateBodyWrapper(
                                            PageWrapper(
                                                    LimitWrapper(
                                                            //                                new WhereWrapper(
                                                            baseResourceType)
                                            ),
                                            GroovyTemplateGenerator("EMPTY BODY ..."))
                                    //                        dataSource)
                            ))
                    list.add(resourceType)
                }
            }
        }
        return list
    }

    override fun aliases(): List<String> {
        throw BootException("Not implemented!")
    }

    override fun path(): List<String> {
        throw BootException("Not implemented!")
    }

    @Throws(BootException::class)
    override fun read(uri: Uri): Stream<DbResource> {
        return resourceTypes!!.stream()
                .filter { v -> v.name().equals(uri.type(), ignoreCase = true) }
                .findAny()
                .get()
                .read(uri)
    }

    override fun metaData(): Map<String, String> {
        throw BootException("Not implemented!")
    }

}
