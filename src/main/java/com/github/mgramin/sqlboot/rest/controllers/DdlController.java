/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 mgramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.github.mgramin.sqlboot.rest.controllers;


import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.model.DbResourceCommand;
import com.github.mgramin.sqlboot.model.DbResourceType;
import com.github.mgramin.sqlboot.model.DbUri;
import com.github.mgramin.sqlboot.script.aggregators.Aggregator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ImportResource("classpath:config.xml")
public class DdlController {

    @Autowired
    private List<DbResourceType> types;

    @Autowired
    private List<Aggregator> aggregators;

    @Autowired
    private List<DbResourceCommand> objectCommands;

    private final static Logger logger = Logger.getLogger(DdlController.class);


    @RequestMapping(value = "/api/**", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getTextDdl(HttpServletRequest request,
        @RequestParam(value = "type", required = false) String aggregatorName) throws SqlBootException {
        String uriString;
        if (request.getQueryString() == null || request.getQueryString().isEmpty()) {
            uriString = request.getServletPath();
        } else {
            uriString = request.getServletPath() + "?" + request.getQueryString();
        }

        Aggregator aggregator = aggregators.stream()
            .filter(c -> c.name().equalsIgnoreCase(aggregatorName))
            .findFirst()
            .orElse(null);
        if (aggregator == null)
            aggregator = aggregators.stream()
                .filter(Aggregator::isDefault)
                .findFirst()
                .orElse(null);

        HttpHeaders headers = new HttpHeaders();
        aggregator.httpHeaders().entrySet().forEach(h -> headers.add(h.getKey(), h.getValue()));

        byte[] result = aggregator.aggregate(getDbSchemaObjects(uriString.substring(5), aggregator.name()));
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    private List<DbResource> getDbSchemaObjects(String uriString, String aggregatorName) throws SqlBootException {
        DbUri uri = new DbUri(uriString);

        DbResourceCommand currentCommand;

        if (uri.action() != null) {
            currentCommand = objectCommands.stream().filter(c -> c.aliases().contains(uri.action())).findFirst().orElse(null);
        } else {
            currentCommand = objectCommands.stream().filter(c -> c.isDefault()).findFirst().orElse(null);
        }

        DbResourceType type = types.stream().filter(n -> n.aliases() != null && n.aliases().contains(uri.type())).findFirst().orElse(null);
        if (type == null) return null;

        List<DbResource> objects = type.read(uri, currentCommand, aggregatorName);
        return objects;
    }

}