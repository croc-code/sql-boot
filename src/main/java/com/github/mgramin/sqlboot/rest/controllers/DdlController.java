package com.github.mgramin.sqlboot.rest.controllers;


import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.*;
import com.github.mgramin.sqlboot.readers.IDBObjectReader;
import com.github.mgramin.sqlboot.script.aggregators.IAggregator;
import com.github.mgramin.sqlboot.uri.ObjURI;
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
    private List<DBSchemaObjectType> types;

    @Autowired
    private List<IAggregator> aggregators;

    @Autowired
    private List<DBSchemaObjectCommand> objectCommands;


    @RequestMapping(value = "/ddl/**", method = RequestMethod.GET)
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

    private List<DBSchemaObject> getDbSchemaObjects(String uriString, String aggregatorName) throws SqlBootException {
        ObjURI uri = new ObjURI(uriString);

        DBSchemaObjectCommand currentCommand;

        if (uri.getAction() != null) {
            currentCommand = objectCommands.stream().filter(c -> c.aliases.contains(uri.getAction())).findFirst().orElse(null);
        } else {
            currentCommand = objectCommands.stream().filter(c -> c.isDefault).findFirst().orElse(null);
        }

        DBSchemaObjectType type = types.stream().filter(n -> n.aliases != null && n.aliases.contains(uri.getType())).findFirst().orElse(null);
        if (type == null) return null;

        IDBObjectReader reader = type.readers.stream()
            .findFirst()
            .orElse(null);
        Map<String, DBSchemaObject> objects = reader.readr(uri, type);
        List<DBSchemaObject> objectsNew = new ArrayList();
        for (DBSchemaObject object : objects.values()) {
            if (object.getType().equals(type) || uri.getRecursive()) {
                ObjectService objectService = new ObjectService(objects, String.join(".", object.objURI.getObjects()));

                if (object.type.aggregators != null) {
                    DBSchemaObjectTypeAggregator objectTypeAggregator = object.type.aggregators.stream().filter(a -> a.getAggregatorName().equalsIgnoreCase(aggregatorName)).findFirst().orElse(null);
                    if (objectTypeAggregator != null) {
                        IActionGenerator currentGenerator = object.type.aggregators.stream()
                            .filter(a -> a.getAggregatorName().equalsIgnoreCase(aggregatorName))
                            .findFirst()
                            .orElseGet(null)
                            .getCommands()
                            .stream()
                            .filter(c -> c.getDBSchemaObjectCommand().name.equalsIgnoreCase(
                                currentCommand.name))
                            .findFirst()
                            .orElse(null);

                        if (currentGenerator != null) {
                            Map<String, Object> variables = new TreeMap<>(object.paths);
                            variables.put(object.getType().name, object);
                            variables.put("srv", objectService);

                            object.ddl = currentGenerator.generate(variables);
                            objectsNew.add(object);
                        }
                    }
                }
            }
        }
        return objectsNew;
    }

}