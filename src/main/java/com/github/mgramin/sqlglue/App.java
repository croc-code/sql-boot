package com.github.mgramin.sqlglue;

import com.github.mgramin.sqlglue.actions.generator.IActionGenerator;
import com.github.mgramin.sqlglue.exceptions.GlueException;
import com.github.mgramin.sqlglue.model.DBSchemaObject;
import com.github.mgramin.sqlglue.model.DBSchemaObjectType;
import com.github.mgramin.sqlglue.model.ObjectService;
import com.github.mgramin.sqlglue.scanners.IObjectScanner;
import com.github.mgramin.sqlglue.uri.ObjURI;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by maksim on 18.05.16.
 */
public class App {

    public void main(String[] args) throws GlueException {

        // TODO create user filtering rules (e.g. for Cassandra, LIKE etc ...)

        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        context.getEnvironment().setActiveProfiles("information_schema");
        context.load("config.xml");
        context.refresh();

        ObjURI uri = new ObjURI("t/hr");
        DBSchemaObjectType type = context.getBean(uri.getType(), DBSchemaObjectType.class);

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
                        System.out.println(command.generate(test));
                    }
                }
            }

        }

    }

}