/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Maksim Gramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.rest.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.github.mgramin.sqlboot.aggregators.DbResourceAggregator;
import com.github.mgramin.sqlboot.aggregators.wrappers.HttpWrapper;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.resource.DbResource;
import com.github.mgramin.sqlboot.uri.impl.DbUri;
import com.github.mgramin.sqlboot.model.IDbResourceCommand;
import com.github.mgramin.sqlboot.resource_type.ResourceType;
import com.github.mgramin.sqlboot.uri.Uri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ImportResource("classpath:config.xml")
public final class ApiController {

    @Autowired
    private List<ResourceType> types;

    @Autowired
    private List<HttpWrapper> httpAggregators;

    @Deprecated
    @Autowired
    private List<IDbResourceCommand> commands;

    //    private final static Logger logger = Logger.getLogger(ApiController.class);


    @RequestMapping(value = "/api/**", method = RequestMethod.GET,
    consumes = MediaType.ALL_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<byte[]> getTextDdl(HttpServletRequest request,
        @RequestParam(value = "type", required = false) String aggregatorName) throws BootException {
        String uriString;
        if (request.getQueryString() == null || request.getQueryString().isEmpty()) {
            uriString = request.getServletPath();
        } else {
            uriString = request.getServletPath() + "?" + request.getQueryString();
        }

        HttpWrapper aggregator = httpAggregators.stream()
            .filter(a -> a.name().equalsIgnoreCase(aggregatorName))
            .findFirst()
            .orElse(null);
        if (aggregator == null) {
            aggregator = httpAggregators.stream()
                .filter(DbResourceAggregator::isDefault)
                .findFirst()
                .orElse(null);
        }

        final HttpHeaders headers = new HttpHeaders();
        aggregator.responseHeaders().forEach(headers::add);

        List<DbResource> dbSchemaObjects = getDbSchemaObjects(uriString.substring(5), aggregator.name());
        if (dbSchemaObjects.isEmpty()) {
            return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
        } else {
            byte[] result = aggregator.aggregate(dbSchemaObjects);
            if (dbSchemaObjects.size() == 1) {
                dbSchemaObjects.get(0).headers().forEach(headers::add);
            }
            return new ResponseEntity<>(result, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/api/**",
            method = RequestMethod.GET,
            consumes = MediaType.TEXT_HTML_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<byte[]> getTextHtml(HttpServletRequest request,
                                             @RequestParam(value = "type", required = false) String aggregatorName) throws BootException {

        String uriString;
        if (request.getQueryString() == null || request.getQueryString().isEmpty()) {
            uriString = request.getServletPath();
        } else {
            uriString = request.getServletPath() + "?" + request.getQueryString();
        }

        final HttpWrapper aggregator = httpAggregators.stream()
                .filter(a -> a.name().equalsIgnoreCase("html"))
                .findFirst()
                .orElse(null);

        final HttpHeaders headers = new HttpHeaders();
        aggregator.responseHeaders().forEach(headers::add);

        List<DbResource> dbSchemaObjects = getDbSchemaObjects(uriString.substring(5), aggregator.name());
        if (dbSchemaObjects.isEmpty()) {
            return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
        } else {
            byte[] result = aggregator.aggregate(dbSchemaObjects);
            if (dbSchemaObjects.size() == 1) {
                dbSchemaObjects.get(0).headers().forEach(headers::add);
            }
            return new ResponseEntity<>(result, headers, HttpStatus.OK);
        }
    }

/*    @RequestMapping(value = "/api*//**",
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getTextZip(HttpServletRequest request,
                                             @RequestParam(value = "type", required = false) String aggregatorName) throws BootException {

        String uriString;
        if (request.getQueryString() == null || request.getQueryString().isEmpty()) {
            uriString = request.getServletPath();
        } else {
            uriString = request.getServletPath() + "?" + request.getQueryString();
        }

        final HttpWrapper aggregator = httpAggregators.stream()
                .filter(a -> a.name().equalsIgnoreCase("zip"))
                .findFirst()
                .orElse(null);

        final HttpHeaders headers = new HttpHeaders();
        aggregator.responseHeaders().forEach(headers::add);

        List<DbResource> dbSchemaObjects = getDbSchemaObjects(uriString.substring(5), aggregator.name());
        byte[] result = aggregator.aggregate(dbSchemaObjects);
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }*/


    @RequestMapping(value = "/api/**",
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<byte[]> getTextXml(HttpServletRequest request,
                                              @RequestParam(value = "type", required = false) String aggregatorName) throws BootException {

        String uriString;
        if (request.getQueryString() == null || request.getQueryString().isEmpty()) {
            uriString = request.getServletPath();
        } else {
            uriString = request.getServletPath() + "?" + request.getQueryString();
        }

        final HttpWrapper aggregator = httpAggregators.stream()
                .filter(a -> a.name().equalsIgnoreCase("xml"))
                .findFirst()
                .orElse(null);

        final HttpHeaders headers = new HttpHeaders();
        aggregator.responseHeaders().forEach(headers::add);

        List<DbResource> dbSchemaObjects = getDbSchemaObjects(uriString.substring(5), aggregator.name());
        if (dbSchemaObjects.isEmpty()) {
            return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
        } else {
            byte[] result = aggregator.aggregate(dbSchemaObjects);
            if (dbSchemaObjects.size() == 1) {
                dbSchemaObjects.get(0).headers().forEach(headers::add);
            }
            return new ResponseEntity<>(result, headers, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/api/**",
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> getTextJson(HttpServletRequest request,
                                             @RequestParam(value = "type", required = false) String aggregatorName) throws BootException {

        String uriString;
        if (request.getQueryString() == null || request.getQueryString().isEmpty()) {
            uriString = request.getServletPath();
        } else {
            uriString = request.getServletPath() + "?" + request.getQueryString();
        }

        final HttpWrapper aggregator = httpAggregators.stream()
                .filter(a -> a.name().equalsIgnoreCase("json"))
                .findFirst()
                .orElse(null);

        final HttpHeaders headers = new HttpHeaders();
        aggregator.responseHeaders().forEach(headers::add);

        List<DbResource> dbSchemaObjects = getDbSchemaObjects(uriString.substring(5), aggregator.name());
        if (dbSchemaObjects.isEmpty()) {
            return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
        } else {
            byte[] result = aggregator.aggregate(dbSchemaObjects);
            if (dbSchemaObjects.size() == 1) {
                dbSchemaObjects.get(0).headers().forEach(headers::add);
            }
            return new ResponseEntity<>(result, headers, HttpStatus.OK);
        }
    }







    private List<DbResource> getDbSchemaObjects(String uriString, String aggregatorName) throws BootException {
        final Uri uri = new DbUri(uriString);
        final IDbResourceCommand command;
        if (uri.action() != null) {
            command = commands.stream().filter(c -> c.aliases().contains(uri.action()))
                .findFirst().orElse(null);
        } else {
            command = commands.stream().filter(IDbResourceCommand::isDefault).findFirst()
                .orElse(null);
        }

        final ResourceType type = types.stream()
            .filter(n -> n.aliases() != null && n.aliases().contains(uri.type())).findFirst()
            .orElse(null);
        if (type == null) {
            // TODO make error message as http-response
            return null;
        }
        List<DbResource> dbResources = type.read(uri, command, aggregatorName);

        return dbResources;
    }

}