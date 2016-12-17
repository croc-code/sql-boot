package com.github.mgramin.sqlboot.rest;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.model.DBSchemaObjectType;
import com.github.mgramin.sqlboot.model.DBSchemaObjectTypeContainer;
import com.github.mgramin.sqlboot.model.ObjectService;
import com.github.mgramin.sqlboot.scanners.IObjectScanner;
import com.github.mgramin.sqlboot.uri.ObjURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ImportResource;
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


    @RequestMapping("/ddl/**")
    public byte[] getDdl(HttpServletRequest request, HttpServletResponse response) throws SqlBootException {

        String s = request.getServletPath().toString();
        ObjURI uri = new ObjURI(s.substring(5).replace("*", "%"));

        DBSchemaObjectType type = container.types.stream().filter(n -> n.name.equals(uri.getType())).findFirst().get();

        IObjectScanner scanner = type.scanners.stream().findFirst().get();

        Map<String, DBSchemaObject> objects = scanner.scanr(uri, type);

        List<DBSchemaObject> objectsNew = new ArrayList();

        for (DBSchemaObject object : objects.values()) {
            ObjectService objectService = new ObjectService(objects, String.join(".", object.objURI.getObjects()));
            if (object.type.commands == null)
                continue;
            IActionGenerator command = object.type.commands.stream().filter(n -> n.getAction().name.equals(uri.getAction())).findFirst().get();
            Map<String, Object> test = new TreeMap<>();
            test.putAll(object.paths);
            test.put("srv", objectService);
            object.ddl = command.generate(test);
            objectsNew.add(object);
        }

        response.setHeader("Content-Disposition", "inline;");
        //response.setContentType("application/pdf");

        //return builder.toString();
        return new TextAggregator().aggregate(objectsNew);
    }

}