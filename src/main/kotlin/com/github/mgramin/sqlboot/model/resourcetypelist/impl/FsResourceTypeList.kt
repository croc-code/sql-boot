package com.github.mgramin.sqlboot.model.resourcetypelist.impl

import com.github.mgramin.sqlboot.model.connection.Endpoint
import com.github.mgramin.sqlboot.model.dialect.Dialect
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.resourcetype.impl.SqlResourceType
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.body.BodyWrapper
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.header.SelectWrapper
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.header.TypeWrapper
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.list.SortWrapper
import com.github.mgramin.sqlboot.model.resourcetypelist.ResourceTypeList
import com.github.mgramin.sqlboot.template.generator.impl.FakeTemplateGenerator
import com.github.mgramin.sqlboot.tools.files.file.impl.MarkdownFile
import com.github.mgramin.sqlboot.tools.files.file.impl.SimpleFile
import org.springframework.core.io.FileSystemResource
import java.io.File
import java.nio.charset.StandardCharsets

// From SQL files
class FsResourceTypeList(private val endpoints: List<Endpoint>,
                         private val dialects: List<Dialect>) : ResourceTypeList {

    override fun types() = resourceTypes

    private val resourceTypes: List<ResourceType> =
            File(FileSystemResource(endpoints.first().confDir()).file.path)
                    .walkTopDown()
                    .filter { it.isFile }
                    .filter { it.extension.equals("sql", true) || it.extension.equals("md", true) }
                    .map {
                        if (it.extension.equals("md", true))
                            MarkdownFile(it.name, it.readText(StandardCharsets.UTF_8))
                        else SimpleFile(it.name, listOf(it.readText(StandardCharsets.UTF_8)))
                    }
                    .filter { it.content().isNotEmpty() }
                    .map { it.content().asSequence().map { content -> Pair(it.name(), content) } }
                    .flatMap { it }
                    .map { createObjectType(it) }
                    .toList()

    private fun createObjectType(it: Pair<String, String>) =
            TypeWrapper(
                    SelectWrapper(
                            SortWrapper(
                                    BodyWrapper(
                                            SqlResourceType(
                                                    name = it.first,
                                                    sql = it.second,
                                                    endpoints = endpoints,
                                                    dialects = dialects),
                                            templateGenerator = FakeTemplateGenerator("[EMPTY BODY]")))))

}
