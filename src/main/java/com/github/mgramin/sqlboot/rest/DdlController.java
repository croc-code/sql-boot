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
import java.util.Map;
import java.util.TreeMap;

@RestController
@ImportResource("classpath:config.xml")
public class DdlController {

    @Autowired
    @Qualifier("container")
    DBSchemaObjectTypeContainer container;


    @RequestMapping("/ddl/**")
    public String getDdl(HttpServletRequest request) throws SqlBootException {

        String s = request.getServletPath() .toString() /*+ "?" + request.getQueryString()*/;
        ObjURI uri = new ObjURI(s.substring(5).replace("*", "%"));

        DBSchemaObjectType type = container.types.stream().filter(n -> n.name.equals(uri.getType())).findFirst().get();

        StringBuilder builder = new StringBuilder();

        IObjectScanner scanner = type.scanners.stream().findFirst().get();

        Map<String, DBSchemaObject> objects = scanner.scanr(uri, type);

        for (DBSchemaObject object : objects.values()) {
            ObjectService objectService = new ObjectService(objects, String.join(".", object.objURI.getObjects()));
            if (object.type.commands != null) {
                for (IActionGenerator command : object.type.commands) {
                    if (command.getAction() != null && command.getAction().aliases.contains(uri.getAction())) {
                        Map<String, Object> test = new TreeMap<>();
                        test.putAll(object.paths);
                        test.put("srv", objectService);
                        String generate = command.generate(test);
                        builder.append(generate).append("\n");
                        System.out.println(generate);
                        System.out.println("****************");
                    }
                }
            }

        }



        return builder.toString();

        //return type.name;

//        return String.valueOf(type);


    }

}