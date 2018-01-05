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

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.composite.FsResourceTypes;
import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import com.github.mgramin.sqlboot.model.uri.wrappers.SqlPlaceholdersWrapper;
import io.swagger.models.Info;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@RestController
@ComponentScan(basePackages = "com.github.mgramin.sqlboot.model.resource_type")
@EnableAutoConfiguration
@CrossOrigin
public class ApiController {

    @Autowired
    FsResourceTypes fsResourceTypes;

    @RequestMapping(method = GET, path = "/api", produces = APPLICATION_JSON_VALUE)
    public String apiDocsDEfault(final HttpServletRequest request,
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

    private Swagger getSwaggerDescription(HttpServletRequest request,
        String connectionName) throws JsonProcessingException {
        fsResourceTypes.init(connectionName);
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
            swagger.path("/api/{connection_name}/" + resourceType.name(),
                new Path().get(
                    new Operation().description(resourceType.name()).tag("db_objects").parameter(parameter)
                        .response(200, new Response()
                                .description("Ok")
                                .schema(new ArrayProperty(new RefProperty(resourceType.name()))))
                        .produces("application/json")));
        }

        // definitions
        for (ResourceType resourceType : resourceTypes) {
            ModelImpl model = new ModelImpl();
            Map<String, String> stringStringMap = resourceType.medataData();
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
    public ResponseEntity<List<DbResource>> getResourcesEntireJson(final HttpServletRequest request,
        @PathVariable String connectionName,
        @PathVariable String type) {
        final Uri uri = new SqlPlaceholdersWrapper(new DbUri(parseUri(type, request)));
        fsResourceTypes.init(connectionName);
        final List<DbResource> collect = fsResourceTypes.read(uri)
            .collect(Collectors.toList());
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }


    @RequestMapping(value = "/api/{connectionName}/{type}/{path:.+}")
    public ResponseEntity<List<DbResource>> getResourcesEntireJson2(final HttpServletRequest request,
        @PathVariable String connectionName,
        @PathVariable String type,
        @PathVariable String path) {
        final Uri uri = new SqlPlaceholdersWrapper(new DbUri(parseUri(type + "/" + path, request)));
        fsResourceTypes.init(connectionName);
        final List<DbResource> collect = fsResourceTypes.read(uri)
            .collect(Collectors.toList());
        return new ResponseEntity<>(collect, HttpStatus.OK);
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
        @PathVariable String path) throws BootException, IOException {
        final Uri uri = new SqlPlaceholdersWrapper(new DbUri(parseUri(type + "/" + path, request)));
        fsResourceTypes.init(connectionName);
        final List<Map<String, Object>> headers = fsResourceTypes
            .read(uri)
            .map(DbResource::headers)
            .collect(toList());
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/{connectionName}/headers/{type}", method = GET)
    public ResponseEntity<List<Map<String, Object>>> getResourcesHeadersJson2(
        final HttpServletRequest request,
        @PathVariable String connectionName,
        @PathVariable String type) throws BootException, IOException {
        final Uri uri = new SqlPlaceholdersWrapper(new DbUri(parseUri(type, request)));
        fsResourceTypes.init(connectionName);
        final List<Map<String, Object>> headers = fsResourceTypes
            .read(uri)
            .map(DbResource::headers)
            .collect(toList());
        return new ResponseEntity<>(headers, HttpStatus.OK);
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
