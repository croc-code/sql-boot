package com.github.mgramin.sqlboot.rest.controllers;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.model.DBSchemaObjectType;
import com.github.mgramin.sqlboot.model.DBSchemaObjectTypeContainer;
import com.github.mgramin.sqlboot.model.ObjectService;
import com.github.mgramin.sqlboot.readers.IDBObjectReader;
import com.github.mgramin.sqlboot.script.aggregators.AggregatorContainer;
import com.github.mgramin.sqlboot.script.aggregators.IAggregator;
import com.github.mgramin.sqlboot.uri.ObjURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @Qualifier("container")
    DBSchemaObjectTypeContainer container;

    @Autowired
    private AggregatorContainer aggregatorContainer;


    @RequestMapping(value = "/ddl/**", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getTextDdl(HttpServletRequest request) throws SqlBootException {
        String servletPath = request.getServletPath().toString();
        String type = request.getParameter("type");
        IAggregator aggregator = aggregatorContainer.getAggregators().stream().filter(c -> c.getName().equalsIgnoreCase(type)).findFirst().orElse(null);
        if (aggregator == null)
            aggregator = aggregatorContainer.getAggregators().stream().filter(c -> c.getIsDefault()).findFirst().get();
        HttpHeaders responseHeaders = new HttpHeaders();
        for (Map.Entry<String, String> s : aggregator.getHttpHeaders().entrySet()) {
            responseHeaders.add(s.getKey(), s.getValue());
        }

        return new ResponseEntity<>(aggregator.aggregate(getDbSchemaObjects(servletPath)), responseHeaders, HttpStatus.OK);
    }

    private List<DBSchemaObject> getDbSchemaObjects(String s) throws SqlBootException {
        ObjURI uri = new ObjURI(s.substring(5).replace("*", "%"));
        DBSchemaObjectType type = container.types.stream().filter(n -> n.name.equals(uri.getType())).findFirst().get();
        IDBObjectReader reader = type.readers.stream().findFirst().get();
        Map<String, DBSchemaObject> objects = reader.readr(uri, type);
        List<DBSchemaObject> objectsNew = new ArrayList();
        for (DBSchemaObject object : objects.values()) {
            if (object.getType().equals(type) || uri.getRecursive()) {
                ObjectService objectService = new ObjectService(objects, String.join(".", object.objURI.getObjects()));
                if (object.type.commands == null) continue;
                IActionGenerator command = object.type.commands.stream().filter(n -> n.getDBSchemaObjectCommand().name.equals(uri.getAction())).findFirst().get();
                Map<String, Object> variables = new TreeMap<>(object.paths);
                variables.put(object.getType().name, object);
                variables.put("srv", objectService);
                object.ddl = command.generate(variables);
                objectsNew.add(object);
            }
        }
        return objectsNew;
    }

}