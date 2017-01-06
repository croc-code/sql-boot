package com.github.mgramin.sqlboot.rest.controllers;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.model.DBSchemaObjectType;
import com.github.mgramin.sqlboot.model.DBSchemaObjectTypeContainer;
import com.github.mgramin.sqlboot.model.ObjectService;
import com.github.mgramin.sqlboot.readers.IDBObjectReader;
import com.github.mgramin.sqlboot.script.aggregators.TextAggregator;
import com.github.mgramin.sqlboot.script.aggregators.ZipAggregator;
import com.github.mgramin.sqlboot.uri.ObjURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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


    @RequestMapping(value = "/ddl/**", method = RequestMethod.GET)
    public byte[] getTextDdl(HttpServletRequest request, HttpServletResponse response) throws SqlBootException {
        String s = request.getServletPath().toString();
        response.setHeader("Content-Disposition", "inline;");
        //response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        //response.setContentType(MediaType.TEXT_HTML_VALUE);
        return new TextAggregator().aggregate(getDbSchemaObjects(s));
    }

    @RequestMapping(value = "/ddl/**", params = {"type=zip"}, method = RequestMethod.GET, produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public byte[] getZipDdl(HttpServletRequest request, HttpServletResponse response) throws SqlBootException {
        String s = request.getServletPath().toString();
        response.setHeader("Content-Disposition", "attachment; filename=result.zip");
        return new ZipAggregator().aggregate(getDbSchemaObjects(s));
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
                if (object.type.commands == null)
                    continue;
                IActionGenerator command = object.type.commands.stream().filter(n -> n.getDBSchemaObjectCommand().name.equals(uri.getAction())).findFirst().get();
                Map<String, Object> test = new TreeMap<>();
                test.putAll(object.paths);
                test.put(object.getType().name, object);
                test.put("srv", objectService);
                object.ddl = command.generate(test);
                objectsNew.add(object);
            }
        }
        return objectsNew;
    }

}