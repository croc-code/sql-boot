package com.github.mgramin.sqlboot.util.template_engine.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.util.template_engine.TemplateEngine;
import groovy.text.Template;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MGramin on 26.02.2017.
 */
public class AbstractGroovyTemplateEngine implements TemplateEngine {

    protected groovy.text.TemplateEngine engine;
    protected Template template;


    @Override
    public void setTemplate(String template) {
        try {
            this.template = engine.createTemplate(template.replace("!{", "${"));
        } catch (ClassNotFoundException | IOException e) {
            throw new SqlBootException(e);
        }
    }

    @Override
    public String process(Map<String, Object> variables) {
        return this.template.make(variables).toString();
    }

    @Override
    public List<String> getAllProperties(String templateText) throws SqlBootException {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\$\\s*(\\w+)");
        Matcher matcher = pattern.matcher(templateText.replace("!", "$"));
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }

}
