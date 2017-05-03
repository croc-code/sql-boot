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


import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.*;
import com.github.mgramin.sqlboot.readers.DbResourceReader;
import com.github.mgramin.sqlboot.script.aggregators.IAggregator;
import com.github.mgramin.sqlboot.uri.ObjURI;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@ImportResource("classpath:config.xml")
public class DdlController {

    @Autowired
    private List<DBResourceType> types;

    @Autowired
    private List<IAggregator> aggregators;

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

        IAggregator aggregator = aggregators.stream()
            .filter(c -> c.getName().equalsIgnoreCase(aggregatorName))
            .findFirst()
            .orElse(null);
        if (aggregator == null)
            aggregator = aggregators.stream()
                .filter(IAggregator::getIsDefault)
                .findFirst()
                .orElse(null);

        HttpHeaders headers = new HttpHeaders();
        aggregator.getHttpHeaders().entrySet().forEach(h -> headers.add(h.getKey(), h.getValue()));

        byte[] result = aggregator.aggregate(getDbSchemaObjects(uriString.substring(5), aggregator.getName()));
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    private List<DbResource> getDbSchemaObjects(String uriString, String aggregatorName) throws SqlBootException {
        ObjURI uri = new ObjURI(uriString);

        DbResourceCommand currentCommand;

        if (uri.getAction() != null) {
            currentCommand = objectCommands.stream().filter(c -> c.aliases().contains(uri.getAction())).findFirst().orElse(null);
        } else {
            currentCommand = objectCommands.stream().filter(c -> c.isDefault()).findFirst().orElse(null);
        }

        DBResourceType type = types.stream().filter(n -> n.aliases != null && n.aliases.contains(uri.getType())).findFirst().orElse(null);
        if (type == null) return null;

        DbResourceReader reader = type.readers.stream()
            .findFirst()
            .orElse(null);
        Map<String, DbResource> objects = reader.readr(uri, type);
        List<DbResource> objectsNew = new ArrayList();
        for (DbResource object : objects.values()) {
            if (object.getType().equals(type) || uri.getRecursive()) {
                ObjectService objectService = new ObjectService(objects, String.join(".", object.objURI.getObjects()));

                if (object.type.aggregators != null) {
                    DbSchemaObjectTypeAggregator objectTypeAggregator = object.type.aggregators.stream().filter(a -> a.getAggregatorName().contains(aggregatorName)).findFirst().orElse(null);
                    if (objectTypeAggregator != null) {
                        ActionGenerator currentGenerator = object.type.aggregators.stream()
                            .filter(a -> a.getAggregatorName().contains(aggregatorName))
                            .findFirst()
                            .orElseGet(null)
                            .getCommands()
                            .stream()
                            .filter(c -> c.command().name().equalsIgnoreCase(
                                currentCommand.name()))
                            .findFirst()
                            .orElse(null);

                        if (currentGenerator != null) {
                            Map<String, Object> variables = new TreeMap<>(object.paths);
                            variables.put(object.getType().name, object);
                            variables.put("srv", objectService);

                            object.body = currentGenerator.generate(variables);
                            objectsNew.add(object);
                        }
                    }
                }
            }
        }
        return objectsNew;
    }

}