/**
 * The MIT License (MIT) <p> Copyright (c) 2016-2017 Maksim Gramin <p> Permission is hereby granted,
 * free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions: <p> The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software. <p> THE SOFTWARE IS PROVIDED "AS IS",
 * WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.mgramin.sqlboot.rest.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mgramin.sqlboot.model.connection.DbConnectionList;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.composite.FsResourceTypes;
import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import com.github.mgramin.sqlboot.model.uri.wrappers.SqlPlaceholdersWrapper;
import io.swagger.models.*;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@RestController
@ComponentScan(basePackages = "com.github.mgramin.sqlboot")
@EnableAutoConfiguration
@CrossOrigin
public class ApiController {

    @Autowired
    private DbConnectionList dbConnectionList;

    @RequestMapping(method = GET, path = "/api", produces = APPLICATION_JSON_VALUE)
    public String apiDocsDefault(final HttpServletRequest request,
                                 @RequestParam(required = false) final String format) throws JsonProcessingException {
        final String connectionName = "h2";
        final Swagger swaggerDescription = getSwaggerDescription(request, connectionName);
        if (format != null && format.equals("yaml")) {
            return Yaml.pretty().writeValueAsString(swaggerDescription);
        } else {
            return Json.pretty().writeValueAsString(swaggerDescription);
        }
    }

    @RequestMapping(method = GET, path = "/api/{connectionName}", produces = APPLICATION_JSON_VALUE)
    public String apiDocs(final HttpServletRequest request,
                          @PathVariable String connectionName,
                          @RequestParam(required = false) final String format) throws JsonProcessingException {
        final Swagger swaggerDescription = getSwaggerDescription(request, connectionName);
        if (format != null && format.equals("yaml")) {
            return Yaml.pretty().writeValueAsString(swaggerDescription);
        } else {
            return Json.pretty().writeValueAsString(swaggerDescription);
        }
    }

    private Swagger getSwaggerDescription(HttpServletRequest request, String connectionName) {
        FsResourceTypes fsResourceTypes = new FsResourceTypes(dbConnectionList.getConnectionByName(connectionName));
        final List<ResourceType> resourceTypes = fsResourceTypes.resourceTypes();
        Swagger swagger = new Swagger();

        swagger.consumes("application/json");
        swagger.host(request.getServerName() + ":" + request.getServerPort());

        swagger.setInfo(new Info().version("v1").title("API specification"));
        swagger.setSchemes(asList(Scheme.HTTP, Scheme.HTTPS));

        swagger.path("/connections",
                new Path().get(new Operation()
                        .tag("connections")
                        .response(200,
                                new Response()
                                        .description("Ok")
                                        .schema(new ArrayProperty(new RefProperty("connection"))))
                        .produces("application/json")));
        swagger.model("connection", new ModelImpl()
                .property("name", new StringProperty().description("name"))
                .property("url", new StringProperty().description("url"))
                .property("user", new StringProperty().description("user"))
                .property("driverClassName", new StringProperty().description("driverClassName"))
                .property("configurationFolder", new StringProperty().description("configurationFolder"))
        );

        // paths
        for (ResourceType resourceType : resourceTypes) {

            PathParameter parameter = new PathParameter().required(true).type("string").name("connection_name");
            parameter.setDefaultValue(connectionName);
            swagger.path("/api/{connection_name}/headers/" + resourceType.name(),
                    new Path().get(
                            new Operation().description(resourceType.name()).tag(resourceType.name())
                                    .parameter(parameter)
                                    .parameter(new QueryParameter().name("select").type("string"))
                                    .parameter(new QueryParameter().name("distinct").type("boolean"))
                                    .parameter(new QueryParameter().name("where").type("string"))
                                    .parameter(new QueryParameter().name("page").type("string").description("get page by mask [page_count:page_size]"))
                                    .parameter(new QueryParameter().name("limit").type("integer"))
                                    .parameter(new QueryParameter().name("orderby").type("string"))
                                    .parameter(new QueryParameter().name("cache").type("boolean"))
                                    .response(200, new Response()
                                            .description("Ok")
                                            .schema(new ArrayProperty(new RefProperty(resourceType.name()))))
                                    .produces("application/json")));

            final List<String> path = resourceType.path();
            final List<String> newPath = new ArrayList<>();
            for (String s : path) {
                newPath.add(s + "_name");
                List<Parameter> parameterList = new ArrayList<>();
                PathParameter parameterConnection = new PathParameter().required(true).type("string").name("connection_name");
                parameterConnection.setDefaultValue(connectionName);
                parameterList.add(parameterConnection);
                for (String s1 : newPath) {
                    final PathParameter pathParameter = new PathParameter().required(true).type("string").name(s1);
                    pathParameter.setDefaultValue("*");
                    parameterList.add(pathParameter);
                }
                Operation operation = new Operation();
                operation.setParameters(parameterList);
                operation.parameter(new QueryParameter().name("select").type("string"));
                operation.parameter(new QueryParameter().name("distinct").type("boolean"));
                operation.parameter(new QueryParameter().name("where").type("string"));
                operation.parameter(new QueryParameter().name("page").type("string").description("get page by mask [page_count:page_size]"));
                operation.parameter(new QueryParameter().name("limit").type("integer"));
                operation.parameter(new QueryParameter().name("orderby").type("string"));
                operation.parameter(new QueryParameter().name("cache").type("boolean"));
                swagger.path("/api/{connection_name}/headers/" + resourceType.name() + "/" + newPath.stream().map(v -> "{" + v + "}").collect(joining(".")),
                        new Path().get(
                                operation.description(resourceType.name()).tag(resourceType.name())
                                        .response(200, new Response()
                                                .description("Ok")
                                                .schema(new ArrayProperty(new RefProperty(resourceType.name()))))
                                        .produces("application/json")));
            }
        }

        // definitions
        for (ResourceType resourceType : resourceTypes) {
            ModelImpl model = new ModelImpl();
            Map<String, String> stringStringMap = resourceType.metaData();
            if (stringStringMap != null) {
                Set<Entry<String, String>> entries = stringStringMap.entrySet();
                for (Entry<String, String> stringStringEntry : entries) {
                    model.property(stringStringEntry.getKey(), new StringProperty().description(stringStringEntry.getValue()));
                }
            }
            swagger.model(resourceType.name(), model);
        }

        return swagger;
    }


    @RequestMapping(value = "/api/{connectionName}/{type}")
    public ResponseEntity<List<DbResource>> getResourcesEntireJson(
            final HttpServletRequest request,
            @PathVariable final String connectionName,
            @PathVariable final String type) {
        return getListResponseEntity(request, connectionName, type);
    }

    @RequestMapping(value = "/api/default/{type}")
    public ResponseEntity<List<DbResource>> getResourcesEntireJsonDefaultConnection(
            final HttpServletRequest request,
            @PathVariable final String type) {
        return getListResponseEntity(request, null, type);
    }

    @RequestMapping(value = "/api/{connectionName}/{type}/{path:.+}")
    public ResponseEntity<List<DbResource>> getResourcesEntireJson2(
            final HttpServletRequest request,
            @PathVariable final String connectionName,
            @PathVariable final String type,
            @PathVariable final String path) {
        return getListResponseEntity(request, connectionName, type + "/" + path);
    }

    @RequestMapping(value = "/api/default/{type}/{path:.+}")
    public ResponseEntity<List<DbResource>> getResourcesEntireJson2DefaultConnection(
            final HttpServletRequest request,
            @PathVariable final String type,
            @PathVariable final String path) {
        return getListResponseEntity(request, null, type + "/" + path);
    }



//    @RequestMapping(value = "/api/body/**", method = GET)
//    public ResponseEntity<List<SimpleEntry>> getResourcesBodyJson(final HttpServletRequest request)
//        throws BootException, IOException {
//        final Uri uri = new DbUri(parseUri(request).substring(10));
//        fsResourceTypes.init(null);
//        final List<SimpleEntry> bodyList = fsResourceTypes.read(uri)
//            .map(v -> new SimpleEntry(v.dbUri().toString().toLowerCase(), v.body()))
//            .collect(toList());
//        return new ResponseEntity<>(bodyList, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/api/body/**", method = GET, consumes = TEXT_PLAIN_VALUE, produces = TEXT_PLAIN_VALUE)
//    public ResponseEntity<String> getResourcesBodyTextPlain(final HttpServletRequest request)
//        throws BootException, IOException {
//        final Uri uri = new DbUri(parseUri(request).substring(10));
//        fsResourceTypes.init(null);
//        final String collect = fsResourceTypes.read(uri)
//            .map(DbResource::body)
//            .collect(Collectors.joining("\n"));
//        return new ResponseEntity<>(collect, HttpStatus.OK);
//    }


    @RequestMapping(value = "/api/{connectionName}/headers/{type}/{path:.+}", method = GET)
    public ResponseEntity<List<Map<String, Object>>> getResourcesHeadersJson(
            final HttpServletRequest request,
            @PathVariable String connectionName,
            @PathVariable String type,
            @PathVariable String path) {
        return getListResponseEntityHeaders(request, connectionName, type + "/" + path);
    }

    @RequestMapping(value = "/api/{connectionName}/headers/{type}", method = GET)
    public ResponseEntity<List<Map<String, Object>>> getResourcesHeadersJson2(
            final HttpServletRequest request,
            @PathVariable String connectionName,
            @PathVariable String type) {
        return getListResponseEntityHeaders(request, connectionName, type);
    }

    private ResponseEntity<List<DbResource>> getListResponseEntity(
            final HttpServletRequest request,
            final String connectionName,
            final String type) {
        final Uri uri = new SqlPlaceholdersWrapper(new DbUri(parseUri(type, request)));
        final ResourceType fsResourceTypes = new FsResourceTypes(dbConnectionList.getConnectionByName(connectionName));
        final List<DbResource> collect = fsResourceTypes.read(uri).collect(toList());
        if (collect.isEmpty()) {
            return new ResponseEntity<>(collect, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(collect, HttpStatus.OK);
        }
    }

    private ResponseEntity<List<Map<String, Object>>> getListResponseEntityHeaders(
            final HttpServletRequest request,
            final String connectionName,
            final String path) {
        final Uri uri = new SqlPlaceholdersWrapper(new DbUri(parseUri(path, request)));
        ResourceType fsResourceTypes = new FsResourceTypes(dbConnectionList.getConnectionByName(connectionName));
        final List<Map<String, Object>> headers = fsResourceTypes
                .read(uri)
                .map(DbResource::headers)
                .collect(toList());
        if (headers.isEmpty()) {
            return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
    }

    private String parseUri(String path, final HttpServletRequest request) {
        final String uriString;
        if (request.getQueryString() == null || request.getQueryString().isEmpty()) {
            uriString = path;
        } else {
            uriString = path + "?" + request.getQueryString();
        }
        return uriString;
    }

}
