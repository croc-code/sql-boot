package com.github.mgramin.sqlboot.rest;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObjectType;
import com.github.mgramin.sqlboot.util.template_engine.impl.FMTemplateEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@ImportResource("classpath:config.xml")
public class GreetingController {

    @Autowired
    @Qualifier("templateEngine")
    FMTemplateEngine templateEngine;

    @Autowired
    @Qualifier("table")
    DBSchemaObjectType type;


    @RequestMapping("/ddl")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name
    ,HttpServletRequest request) throws SqlBootException {

        /*StringBuilder builder = new StringBuilder();

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
                        String generate = command.generate(test);
                        System.out.println(generate);
                        builder.append(generate).append("\n");
                    }
                }
            }

        }

        return builder.toString();*/

        //return type.name;
        return type.name.toString();

        /*String s = request.getRequestURL().toString() + "?" + request.getQueryString();
        System.out.println(s.substring(5));
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));*/
    }

}