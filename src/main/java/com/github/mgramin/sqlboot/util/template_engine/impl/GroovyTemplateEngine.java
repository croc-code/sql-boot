package com.github.mgramin.sqlboot.util.template_engine.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.util.template_engine.ITemplateEngine;

import java.util.List;
import java.util.Map;

/**
 * Created by MGramin on 28.11.2016.
 */
public class GroovyTemplateEngine implements ITemplateEngine {

    @Override
    public String process(Map<String, Object> variables, String template) throws SqlBootException {
        return null;
    }

    @Override
    public List<String> getAllProperties(String templateText) throws SqlBootException {
        return null;
    }



}
