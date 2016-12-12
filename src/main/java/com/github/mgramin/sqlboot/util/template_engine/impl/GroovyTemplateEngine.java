package com.github.mgramin.sqlboot.util.template_engine.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.util.template_engine.ITemplateEngine;
import groovy.text.GStringTemplateEngine;
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
    public String process(Map<String, Object> variables, String template) throws SqlBootException {
        try {
            return engine.createTemplate(template.replace("!{", "${")).make(variables).toString();
        } catch (ClassNotFoundException | IOException e) {
            throw new SqlBootException(e);
        }
    }

    @Override
    public List<String> getAllProperties(String templateText) throws SqlBootException {
        List<String> result = new ArrayList<>();
        Pattern p = Pattern.compile("\\$\\s*(\\w+)");
        Matcher m = p.matcher(templateText.replace("!", "$"));
        while (m.find())
            result.add(m.group(1));
        return result;
    }

}
