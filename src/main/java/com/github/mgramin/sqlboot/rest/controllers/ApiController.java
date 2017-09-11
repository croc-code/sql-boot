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

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.impl.composite.FsResourceTypes;
import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@RestController
@ComponentScan(basePackages = "com.github.mgramin.sqlboot.model.resource_type")
@EnableAutoConfiguration
public class ApiController {

    @Autowired
    FsResourceTypes types;

    @RequestMapping(method = GET, path = "/api", produces = APPLICATION_JSON_VALUE)
    public Resource apiDocs() {
        return new ClassPathResource("swagger.json");
    }

    @RequestMapping(value = "/api/**", method = GET)
    public ResponseEntity<List<DbResource>> getTextDdl(final HttpServletRequest request)
        throws BootException, IOException {
        final Uri uri = new DbUri(parseUri(request).substring(5));
        types.init();
        final List<DbResource> resources = types.read(uri);
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/body/**", method = GET)
    public ResponseEntity<List<String>> getResourcesBody(final HttpServletRequest request)
        throws BootException, IOException {
        final Uri uri = new DbUri(parseUri(request).substring(10));
        types.init();
        final List<String> bodyList = types.read(uri).stream().map(DbResource::body)
            .collect(toList());
        return new ResponseEntity<>(bodyList, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/headers/**", method = GET)
    public ResponseEntity<List<Map<String, String>>> getResourcesHeaders(
        final HttpServletRequest request) throws BootException, IOException {
        final Uri uri = new DbUri(parseUri(request).substring(13));
        types.init();
        final List<Map<String, String>> headers = types.read(uri).stream().map(DbResource::headers)
            .collect(toList());
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

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

