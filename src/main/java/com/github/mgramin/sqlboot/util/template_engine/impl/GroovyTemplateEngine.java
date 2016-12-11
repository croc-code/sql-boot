package com.github.mgramin.sqlboot.util.template_engine.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.util.template_engine.ITemplateEngine;
import groovy.lang.Writable;
import groovy.text.GStringTemplateEngine;
import groovy.text.SimpleTemplateEngine;
import groovy.text.TemplateEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MGramin on 28.11.2016.
 */
public class GroovyTemplateEngine implements ITemplateEngine {

    private TemplateEngine engine;

    public GroovyTemplateEngine() {
        engine = new GStringTemplateEngine();
    }

    @Override
    public String process(Map<String, Object> variables, String template) {
        template = template.replace("!", "$");
        Writable folderTemplate = null;
        try {
            folderTemplate = engine.createTemplate(template).make(variables);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return folderTemplate.toString();
    }

    @Override
    public List<String> getAllProperties(String templateText) throws SqlBootException {
        templateText = templateText.replace("!", "$");
        List<String> result = new ArrayList<>();
        Pattern p = Pattern.compile("\\$\\s*(\\w+)");
        Matcher m = p.matcher(templateText);
        while (m.find())
        {
            result.add(m.group(1));
        }
        return result;
    }

}
