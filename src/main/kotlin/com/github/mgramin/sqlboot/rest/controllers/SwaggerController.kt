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

package com.github.mgramin.sqlboot.rest.controllers

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.mgramin.sqlboot.model.connection.EndpointList
import com.github.mgramin.sqlboot.model.resourcetype.impl.FsResourceType
import io.swagger.models.*
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
import java.util.*
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

class SwaggerController {

    @Autowired
    private lateinit var endpointList: EndpointList


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
                listOf(endpointList.getByName(connectionName)), emptyList())
        val resourceTypes = fsResourceTypes.resourceTypes()
        val swagger = Swagger()

        swagger.consumes("application/json")
        swagger.host(request.serverName + ":" + request.serverPort)

        swagger.info = Info().version("v1").title("API specification")
        swagger.schemes = Arrays.asList(Scheme.HTTP, Scheme.HTTPS)

        swagger.path("/endpoints",
                Path().get(Operation()
                        .tag("endpoints")
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