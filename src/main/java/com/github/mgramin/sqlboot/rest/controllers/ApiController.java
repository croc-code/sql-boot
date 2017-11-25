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
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.composite.FsResourceTypes;
import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import io.swagger.models.Info;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Yaml;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
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
    public String apiDocs(final HttpServletRequest request,
        @RequestParam(required = false) final String host) throws JsonProcessingException {
        fsResourceTypes.init();
        final List<ResourceType> resourceTypes = fsResourceTypes.resourceTypes();
        Swagger swagger = new Swagger();

        swagger.consumes("application/json");
        swagger.host(ofNullable(host).orElse(request.getRequestURL().toString()));
        swagger.basePath("/");

        swagger.setInfo(new Info().version("v1").title("API specification"));
        swagger.setSchemes(asList(Scheme.HTTP, Scheme.HTTPS));

        for (ResourceType resourceType : resourceTypes) {
            swagger.path(resourceType.name(),
                new Path().get(
                    new Operation()
                        .response(200, new Response().description("Ok").schema(new RefProperty(resourceType.name())))
                        .produces("application/json")));
        }

        for (ResourceType resourceType : resourceTypes) {
            ModelImpl model = new ModelImpl();
            Map<String, String> stringStringMap = resourceType.medataData();
            if (stringStringMap != null) {
                Set<Entry<String, String>> entries = stringStringMap.entrySet();
                for (Entry<String, String> stringStringEntry : entries) {
                    model.property(stringStringEntry.getKey(), new StringProperty().description("Description here ..."));
                }
            }
            swagger.model(resourceType.name(), model);
        }

//        return Json.pretty().writeValueAsString(swagger);
        return Yaml.pretty().writeValueAsString(swagger);
    }



    @RequestMapping(value = "/api/**")
    public ResponseEntity<List<DbResource>> getResourcesEntireJson(final HttpServletRequest request) {
        final Uri uri = new DbUri(parseUri(request).substring(5));
        fsResourceTypes.init();
        final List<DbResource> collect = fsResourceTypes.read(uri)
            .collect(Collectors.toList());
        return new ResponseEntity<List<DbResource>>(collect, HttpStatus.OK);
    }



    @RequestMapping(value = "/api/body/**", method = GET)
    public ResponseEntity<List<SimpleEntry>> getResourcesBodyJson(final HttpServletRequest request)
        throws BootException, IOException {
        final Uri uri = new DbUri(parseUri(request).substring(10));
        fsResourceTypes.init();
        final List<SimpleEntry> bodyList = fsResourceTypes.read(uri)
            .map(v -> new SimpleEntry(v.dbUri().toString().toLowerCase(), v.body()))
            .collect(toList());
        return new ResponseEntity<>(bodyList, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/body/**", method = GET, consumes = TEXT_PLAIN_VALUE, produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<Map<String, String>> getResourcesBodyTextPlain(final HttpServletRequest request)
        throws BootException, IOException {
        final Uri uri = new DbUri(parseUri(request).substring(10));
        fsResourceTypes.init();
        final Map<String, String> bodyList = fsResourceTypes.read(uri)
            .collect(toMap(v -> v.dbUri().toString().toLowerCase(), DbResource::body));
        return new ResponseEntity<>(bodyList, HttpStatus.OK);
    }


    @RequestMapping(value = "/api/headers/**", method = GET)
    public ResponseEntity<List<Map<String, String>>> getResourcesHeadersJson(
        final HttpServletRequest request) throws BootException, IOException {
        final Uri uri = new DbUri(parseUri(request).substring(13));
        fsResourceTypes.init();
        final List<Map<String, String>> headers = fsResourceTypes
            .read(uri)
            .map(DbResource::headers)
            .collect(toList());
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/headers/**", method = GET, consumes = TEXT_PLAIN_VALUE, produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getResourcesHeadersTextPlain(
        final HttpServletRequest request) throws BootException, IOException {
        final Uri uri = new DbUri(parseUri(request).substring(13));
        fsResourceTypes.init();
        String collect = fsResourceTypes.read(uri)
            .map(v -> v.headers().values().stream().collect(joining(","))).collect(joining("\n"));
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }



    @RequestMapping(value = "/api/type/**", method = GET)
    @Deprecated
    public Map<String, String> getTypeMetadata(final HttpServletRequest request)
        throws BootException, IOException {
        final Uri uri = new DbUri(parseUri(request).substring(10));
        fsResourceTypes.init();
        final ResourceType type = fsResourceTypes.type(uri.type());
        return type.medataData();
    }


    /**
     * parse URI
     */
    private String parseUri(final HttpServletRequest request) {
        String uriString;
        if (request.getQueryString() == null || request.getQueryString().isEmpty()) {
            uriString = request.getServletPath();
        } else {
            uriString = request.getServletPath() + "?" + request.getQueryString();
        }
        return uriString;
    }

}
