package com.github.mgramin.sqlglue;

import com.github.mgramin.sqlglue.actions.generator.IActionGenerator;
import com.github.mgramin.sqlglue.model.DBSchemaObject;
import com.github.mgramin.sqlglue.model.DBSchemaObjectType;
import com.github.mgramin.sqlglue.model.IDBSchemaObjectType;
import com.github.mgramin.sqlglue.model.ObjectService;
import com.github.mgramin.sqlglue.uri.ObjURI;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by maksim on 18.05.16.
 */
public class App {

    public static void main(String[] args) {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        /*context.getEnvironment().setActiveProfiles("oracle");*/
        context.getEnvironment().setActiveProfiles("postgres");
        context.load("config.xml");
        context.refresh();

        String uri;

        if (args != null && args.length >= 1) {
            uri = args[0];
        }
        else {
            /*uri = "table/hr";*/
            uri = "i/public.american_football_action_participants%";
        }

        ObjURI objURI = new ObjURI(uri);

        IDBSchemaObjectType type = context.getBean(objURI.getType(), IDBSchemaObjectType.class);

        Map<String, DBSchemaObject> objects = type.scan(objURI.getObjects(), objURI.getAction(), objURI.getRecursive());

        for (Map.Entry<String, DBSchemaObject> object : objects.entrySet()) {
            ObjectService objectService = new ObjectService(objects, String.join(".", object.getValue().getObjURI().getObjects()));
            if (((DBSchemaObjectType) object.getValue().getType()).getCommands() != null) {
                for (IActionGenerator generator : ((DBSchemaObjectType) object.getValue().getType()).getCommands()) {
                    if (generator.getAction() != null && generator.getAction().getAliases().contains(objURI.getAction())) {
                        Map<String, Object> test = new TreeMap<>();
                        test.putAll(object.getValue().getPaths());
                        test.put("srv", objectService);
                        System.out.println(generator.generate(test));
                    }
                }
            }
        }

        for (Map.Entry<String, DBSchemaObject> object : objects.entrySet()) {

        }

        /*if (command.getFilePath() != null) {
            object.getProperties().setProperty("file", "repo/" + templateEngine.process(dataNew, command.getFilePath()));
        }*/

    }

}