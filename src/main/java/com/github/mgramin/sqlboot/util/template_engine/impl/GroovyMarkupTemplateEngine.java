package com.github.mgramin.sqlboot.util.template_engine.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.util.template_engine.ITemplateEngine;
import groovy.text.GStringTemplateEngine;
import groovy.text.TemplateEngine;
import groovy.text.markup.MarkupTemplateEngine;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by mgramin on 06.01.2017.
 */
public class GroovyMarkupTemplateEngine implements ITemplateEngine {

    private TemplateEngine engine;

    public GroovyMarkupTemplateEngine() {
        engine = new MarkupTemplateEngine();
    }


    @Override
    public String process(Map<String, Object> variables, String template) throws SqlBootException {
        try {
            return engine.createTemplate(template).make(variables).toString();
        } catch (ClassNotFoundException | IOException e) {
            throw new SqlBootException(e);
        }
    }

    @Override
    public List<String> getAllProperties(String templateText) throws SqlBootException {
        throw new SqlBootException("Not implemented!");
    }

}