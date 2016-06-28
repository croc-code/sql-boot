package com.github.mgramin.sqlglue;

import com.github.mgramin.sqlglue.model.DBSchemaObject;
import com.github.mgramin.sqlglue.model.IDBSchemaObjectType;

import com.github.mgramin.sqlglue.uri.ObjURI;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by maksim on 18.05.16.
 */
public class App {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("db_config.xml");
        String uri;

        if (args != null && args.length >= 1) {
            uri = args[0];
        }
        else {
            uri = "p/hr/";
        }

        ObjURI objURI = new ObjURI(uri);



        IDBSchemaObjectType dbSchemaObject = context.getBean(objURI.getType(), IDBSchemaObjectType.class);
        List<DBSchemaObject> objects = dbSchemaObject.scan(objURI.getObjects(), objURI.getRecursive(), objURI.getAction());


        
        for (DBSchemaObject object : objects) {
            System.out.println(object.getDdl());
        }

    }

}