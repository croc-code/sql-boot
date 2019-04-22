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
import com.github.mgramin.sqlboot.model.connection.DbConnectionList
import com.github.mgramin.sqlboot.model.resourcetype.impl.FsResourceType
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
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.util.ArrayList
import java.util.Arrays
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

class SwaggerController {

    @Autowired
    private lateinit var dbConnectionList: DbConnectionList


    @RequestMapping(method = [RequestMethod.GET, RequestMethod.POST], path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
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

    @RequestMapping(method = [RequestMethod.GET, RequestMethod.POST], path = ["/api/{connectionName}"], produces = [MediaType.APPLICATION_JSON_VALUE])
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
        val fsResourceTypes = FsResourceType(
                listOf(dbConnectionList.getConnectionByName(connectionName)), emptyList())
        val resourceTypes = fsResourceTypes.resourceTypes()
        val swagger = Swagger()

        swagger.consumes("application/json")
        swagger.host(request.serverName + ":" + request.serverPort)

        swagger.info = Info().version("v1").title("API specification")
        swagger.schemes = Arrays.asList(Scheme.HTTP, Scheme.HTTPS)

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
                                .map { v -> "{$v}" }.collect(Collectors.joining(".")),
                        Path().get(
                                operation.description(resourceType.name()).tag(resourceType.name())
                                        .response(200, Response()
                                                .description("Ok")
                                                .schema(ArrayProperty(RefProperty(resourceType.name()))))
                                        .produces("application/json")))
            }
        }

        // definitions
        /*for (resourceType in resourceTypes) {
            val model = ModelImpl()
            for ((key, value) in resourceType.metaData().entries) {
                model.property(key, StringProperty().description(value))
            }
            swagger.model(resourceType.name(), model)
        }*/

        return swagger
    }

}