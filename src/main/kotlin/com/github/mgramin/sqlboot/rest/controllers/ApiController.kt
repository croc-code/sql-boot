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

package com.github.mgramin.sqlboot.rest.controllers

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.model.connection.DbConnectionList
import com.github.mgramin.sqlboot.model.resource.DbResource
import com.github.mgramin.sqlboot.model.resourcetype.Metadata
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.resourcetype.impl.composite.FsResourceTypes
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.model.uri.impl.FakeUri
import com.github.mgramin.sqlboot.model.uri.wrappers.SqlPlaceholdersWrapper
import io.swagger.models.Info
import io.swagger.models.ModelImpl
import io.swagger.models.Operation
import io.swagger.models.Path
import io.swagger.models.Response
import io.swagger.models.Scheme
import io.swagger.models.Swagger
import io.swagger.models.parameters.Parameter
import io.swagger.models.parameters.PathParameter
import io.swagger.models.parameters.QueryParameter
import io.swagger.models.properties.ArrayProperty
import io.swagger.models.properties.RefProperty
import io.swagger.models.properties.StringProperty
import io.swagger.util.Json
import io.swagger.util.Yaml
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.util.ArrayList
import java.util.Arrays.asList
import java.util.stream.Collectors.joining
import java.util.stream.Collectors.toList
import javax.servlet.http.HttpServletRequest

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 69e9609bd238163b6b97346900c17bb6efd0c037 $
 * @since 0.1
 */
@RestController
@ComponentScan(basePackages = ["com.github.mgramin.sqlboot"])
@EnableAutoConfiguration
@CrossOrigin
class ApiController {

    @Autowired
    private lateinit var dbConnectionList: DbConnectionList

    @RequestMapping(method = [GET, POST], path = ["/api"], produces = [APPLICATION_JSON_VALUE])
    @Throws(JsonProcessingException::class)
    fun apiDocsDefault(
            request: HttpServletRequest,
            @RequestParam(required = false) format: String?
    ): String {
        val connectionName = "h2"
        val swaggerDescription = getSwaggerDescription(request, connectionName)
        return if (format != null && format == "yaml") {
            Yaml.pretty().writeValueAsString(swaggerDescription)
        } else {
            Json.pretty().writeValueAsString(swaggerDescription)
        }
    }

    @RequestMapping(method = [GET, POST], path = ["/api/{connectionName}"], produces = [APPLICATION_JSON_VALUE])
    @Throws(JsonProcessingException::class)
    fun apiDocs(
            request: HttpServletRequest,
            @PathVariable connectionName: String,
            @RequestParam(required = false) format: String?
    ): String {
        val swaggerDescription = getSwaggerDescription(request, connectionName)
        return if (format != null && format == "yaml") {
            Yaml.pretty().writeValueAsString(swaggerDescription)
        } else {
            Json.pretty().writeValueAsString(swaggerDescription)
        }
    }

    private fun getSwaggerDescription(request: HttpServletRequest, connectionName: String): Swagger {
        val fsResourceTypes = FsResourceTypes(
                listOf(dbConnectionList.getConnectionByName(connectionName)), FakeUri())
        val resourceTypes = fsResourceTypes.resourceTypes()
        val swagger = Swagger()

        swagger.consumes("application/json")
        swagger.host(request.serverName + ":" + request.serverPort)

        swagger.info = Info().version("v1").title("API specification")
        swagger.schemes = asList(Scheme.HTTP, Scheme.HTTPS)

        swagger.path("/connections",
                Path().get(Operation()
                        .tag("connections")
                        .response(200,
                                Response()
                                        .description("Ok")
                                        .schema(ArrayProperty(RefProperty("connection"))))
                        .produces("application/json")))
        swagger.model("connection", ModelImpl()
                .property("name", StringProperty().description("name"))
                .property("url", StringProperty().description("url"))
                .property("user", StringProperty().description("user"))
                .property("driverClassName", StringProperty().description("driverClassName"))
                .property("configurationFolder",
                        StringProperty().description("configurationFolder"))
        )

        // paths
        for (resourceType in resourceTypes) {

            val parameter = PathParameter().required(true).type("string")
                    .name("connection_name")
            parameter.setDefaultValue(connectionName)
            swagger.path("/api/{connection_name}/headers/" + resourceType.name(),
                    Path().get(
                            Operation().description(resourceType.name()).tag(resourceType.name())
                                    .parameter(parameter)
                                    .parameter(QueryParameter().name("select").type("string")
                                            .description("Select specific columns"))
                                    .parameter(QueryParameter().name("distinct").type("boolean")
                                            .description("Select unique rows"))
                                    .parameter(QueryParameter().name("where").type("string")
                                            .description("Apply filter"))
                                    .parameter(QueryParameter().name("page").type("string")
                                            .description("Page number by mask [page_number:page_size]"))
                                    .parameter(QueryParameter().name("limit").type("integer")
                                            .description("Limit the number of rows"))
                                    .parameter(QueryParameter().name("orderby").type("string")
                                            .description("Sort rows"))
                                    .parameter(QueryParameter().name("cache").type("boolean")
                                            .description("Cache result"))
                                    .response(200, Response()
                                            .description("Ok")
                                            .schema(ArrayProperty(RefProperty(resourceType.name()))))
                                    .response(204, Response().description("Objects not found ..."))
                                    .produces("application/json")))

            val path = resourceType.path()
            val newPath = ArrayList<String>()
            for (s in path) {
                newPath.add(s + "_name")
                val parameterList = ArrayList<Parameter>()
                val parameterConnection = PathParameter().required(true)
                        .type("string").name("connection_name")
                parameterConnection.setDefaultValue(connectionName)
                parameterList.add(parameterConnection)
                for (s1 in newPath) {
                    val pathParameter = PathParameter().required(true)
                            .type("string").name(s1)
                    pathParameter.setDefaultValue("*")
                    parameterList.add(pathParameter)
                }
                val operation = Operation()
                operation.parameters = parameterList
                operation.parameter(QueryParameter().name("select").type("string")
                        .description("Select specific columns"))
                operation.parameter(QueryParameter().name("distinct").type("boolean")
                        .description("Select unique rows"))
                operation.parameter(QueryParameter().name("where").type("string")
                        .description("Apply filter"))
                operation.parameter(QueryParameter().name("page").type("string")
                        .description("get page by mask [page_count:page_size]"))
                operation.parameter(QueryParameter().name("limit").type("integer")
                        .description("Limit the number of rows"))
                operation.parameter(QueryParameter().name("orderby").type("string")
                        .description("Sort rows"))
                operation.parameter(QueryParameter().name("cache").type("boolean")
                        .description("Cache result"))
                swagger.path(
                        "/api/{connection_name}/headers/" + resourceType.name() + "/" + newPath.stream()
                                .map { v -> "{$v}" }.collect(joining(".")),
                        Path().get(
                                operation.description(resourceType.name()).tag(resourceType.name())
                                        .response(200, Response()
                                                .description("Ok")
                                                .schema(ArrayProperty(RefProperty(resourceType.name()))))
                                        .produces("application/json")))
            }
        }

        // definitions
        for (resourceType in resourceTypes) {
            val model = ModelImpl()
            for ((key, value) in resourceType.metaData().entries) {
                model.property(key, StringProperty().description(value))
            }
            swagger.model(resourceType.name(), model)
        }

        return swagger
    }

    @RequestMapping(value = ["/api/{connectionName}/types"])
    fun test(
            request: HttpServletRequest,
            @PathVariable connectionName: String
    ): List<ResourceType>? {
        val fsResourceTypes = FsResourceTypes(
                dbConnectionList.getConnectionsByMask(connectionName), FakeUri())
        return fsResourceTypes.resourceTypes()
    }

    @RequestMapping(value = ["/api/{connectionName}/{type}"])
    fun getResourcesEntireJson(
            request: HttpServletRequest,
            @PathVariable connectionName: String,
            @PathVariable type: String
    ): ResponseEntity<List<DbResource>> {
        return getListResponseEntity(request, connectionName, type)
    }

    @RequestMapping(value = ["/api/default/{type}"])
    fun getResourcesEntireJsonDefaultConnection(
            request: HttpServletRequest,
            @PathVariable type: String
    ): ResponseEntity<List<DbResource>> {
        return getListResponseEntity(request, null, type)
    }

    @RequestMapping(value = ["/api/{connectionName}/{type}/{path:.+}"])
    fun getResourcesEntireJson2(
            request: HttpServletRequest,
            @PathVariable connectionName: String,
            @PathVariable type: String,
            @PathVariable path: String
    ): ResponseEntity<List<DbResource>> {
        return getListResponseEntity(request, connectionName, "$type/$path")
    }

    @RequestMapping(value = ["/api/{connectionName}/{type}/{path:.+}/{action}"])
    fun getResourcesEntireJson3(
            request: HttpServletRequest,
            @PathVariable connectionName: String,
            @PathVariable type: String,
            @PathVariable path: String,
            @PathVariable action: String
    ): ResponseEntity<List<DbResource>> {
        return getListResponseEntity(request, connectionName, "$type/$path/$action")
    }

    @RequestMapping(value = ["/api/default/{type}/{path:.+}"])
    fun getResourcesEntireJson2DefaultConnection(
            request: HttpServletRequest,
            @PathVariable type: String,
            @PathVariable path: String
    ): ResponseEntity<List<DbResource>> {
        return getListResponseEntity(request, null, "$type/$path")
    }

    @RequestMapping(value = ["/api/{connectionName}/headers/{type}/{path:.+}"], method = [GET, POST])
    fun getResourcesHeadersJson(
            request: HttpServletRequest,
            @PathVariable connectionName: String,
            @PathVariable type: String,
            @PathVariable path: String
    ): ResponseEntity<List<Map<String, Any>>> {
        return getListResponseEntityHeaders(request, connectionName, "$type/$path")
    }

    @RequestMapping(value = ["/api/{connectionName}/headers/{type}"], method = [GET, POST])
    fun getResourcesHeadersJson2(
            request: HttpServletRequest,
            @PathVariable connectionName: String,
            @PathVariable type: String
    ): ResponseEntity<List<Map<String, Any>>> {
        return getListResponseEntityHeaders(request, connectionName, type)
    }

    @RequestMapping(value = ["/api/{connectionName}/headers/txt/{type}"], method = [GET, POST],
            produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    @ResponseBody
    fun getResourcesHeadersTxt(
            request: HttpServletRequest,
            @PathVariable connectionName: String,
            @PathVariable type: String
    ): Flux<String> {
        val uri = SqlPlaceholdersWrapper(DbUri(parseUri(type, request)))
        val connections = dbConnectionList.getConnectionsByMask(connectionName)
        val fsResourceTypes = FsResourceTypes(connections, uri)

//        val tableBuilder = Table.Builder()
//                .withAlignments(Table.ALIGN_RIGHT, Table.ALIGN_LEFT)
//                .withRowLimit(32)
//                .addRow("Index", "Boolean")

        return fsResourceTypes
                .read(uri)
                .map { it.headers() }
                .map {
                    val metaData = fsResourceTypes.metaData()
                    val joinToString: String = metaData
                            .map { row -> it[row.key] }
                            .joinToString(limit = 50, truncated = ".....", postfix = "                         ")
                    return@map joinToString
                }
    }


    @RequestMapping(value = ["/api/{connectionName}/headers/{type}/{path:.+}/{action}"], method = [GET, POST])
    fun getResourcesHeadersJson3(
            request: HttpServletRequest,
            @PathVariable connectionName: String,
            @PathVariable type: String,
            @PathVariable path: String,
            @PathVariable action: String
    ): ResponseEntity<List<Map<String, Any>>> {
        return getListResponseEntityHeaders(request, connectionName,
                "$type/$path/$action")
    }

    @RequestMapping(value = ["/api/{connectionName}/meta/{type}"], method = [GET, POST])
    fun getResourceMetadata(
            request: HttpServletRequest,
            @PathVariable connectionName: String,
            @PathVariable type: String
    ): ResponseEntity<List<Map<String, Any>>> {
        val uri = SqlPlaceholdersWrapper(DbUri(parseUri(type, request)))
        val dbConnection = dbConnectionList.getConnectionsByMask(connectionName)
        val fsResourceTypes = FsResourceTypes(
                dbConnection, uri)
        val resourceType = fsResourceTypes
                .resourceTypes()
                .stream()
                .filter { v -> v.name().equals(type, ignoreCase = true) }
                .findAny()
                .orElse(null) ?: return ResponseEntity(ArrayList(), HttpStatus.NO_CONTENT)
        val metadata = resourceType.metaData(uri)
        val collect = metadata.stream().map { it.properties() }
                .collect(toList())
        return ResponseEntity(collect, HttpStatus.OK)
    }

    @RequestMapping(value = ["/api/{connectionName}/meta/{type}/{path:.+}"], method = [GET, POST])
    fun getResourceMetadata2(
            request: HttpServletRequest,
            @PathVariable connectionName: String,
            @PathVariable type: String,
            @PathVariable path: String
    ): ResponseEntity<List<Map<String, Any>>> {
        val uri = SqlPlaceholdersWrapper(DbUri(parseUri(path, request)))
        val dbConnection = dbConnectionList.getConnectionsByMask(connectionName)
        val fsResourceTypes = FsResourceTypes(
                dbConnection, uri)
        val resourceType = fsResourceTypes
                .resourceTypes()
                .stream()
                .filter { v -> v.name().equals(type, ignoreCase = true) }
                .findAny()
                .orElse(null) ?: return ResponseEntity(ArrayList(), HttpStatus.NO_CONTENT)
        val metadata = resourceType.metaData(uri)
        val collect = metadata.stream()
                //            .filter(v -> v.properties() != null)
                .map { it.properties() }
                .collect(toList())
        return ResponseEntity(collect, HttpStatus.OK)
    }

    @RequestMapping(value = ["/api/{connectionName}/meta/{type}/{path:.+}/{action}"], method = [GET, POST])
    fun getResourceMetadata3(
            request: HttpServletRequest,
            @PathVariable connectionName: String,
            @PathVariable type: String,
            @PathVariable path: String,
            @PathVariable action: String
    ): ResponseEntity<List<Metadata>> {
        val uri = SqlPlaceholdersWrapper(
                DbUri(parseUri("$type/$path/$action", request)))
        val fsResourceTypes = FsResourceTypes(
                listOf(dbConnectionList.getConnectionByName(connectionName)), uri)
        val resourceType = fsResourceTypes
                .resourceTypes()
                .stream()
                .filter { v -> v.name().equals(type, ignoreCase = true) }
                .findAny()
                .orElse(null) ?: return ResponseEntity(ArrayList(), HttpStatus.NO_CONTENT)
        return ResponseEntity(resourceType.metaData(uri), HttpStatus.OK)
    }

    private fun getListResponseEntity(
            request: HttpServletRequest,
            connectionName: String?,
            type: String
    ): ResponseEntity<List<DbResource>> {
        val uri = SqlPlaceholdersWrapper(DbUri(parseUri(type, request)))
        val connections = dbConnectionList.getConnectionsByMask(connectionName!!)
        val result = ArrayList<DbResource>()
        val fsResourceTypes = FsResourceTypes(connections, uri)
        val collect: MutableList<DbResource>?
        try {
            collect = fsResourceTypes.read(uri).collectList().block()
        } catch (e: BootException) {
            if (e.errorCode == 404) {
                return ResponseEntity(result, HttpStatus.NOT_FOUND)
            }
            return ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR)
        }
        result.addAll(collect)

        return if (result.isEmpty()) {
            ResponseEntity(result, HttpStatus.NO_CONTENT)
        } else {
            ResponseEntity(result, HttpStatus.OK)
        }
    }

    private fun getListResponseEntityHeaders(
            request: HttpServletRequest,
            connectionName: String,
            path: String
    ): ResponseEntity<List<Map<String, Any>>> {
        val uri = SqlPlaceholdersWrapper(DbUri(parseUri(path, request)))
        val connections = dbConnectionList.getConnectionsByMask(connectionName)
        val headers = FsResourceTypes(connections, uri)
                .read(uri)
                .map { it.headers() }
                .collectList()
                .block()
        return if (headers.isEmpty()) {
            ResponseEntity(headers, HttpStatus.NO_CONTENT)
        } else {
            ResponseEntity(headers, HttpStatus.OK)
        }
    }

    private fun parseUri(path: String, request: HttpServletRequest): String {
        return if (request.queryString == null || request.queryString.isEmpty()) {
            path
        } else {
            path + "?" + request.queryString
        }
    }
}
