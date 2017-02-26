package com.github.mgramin.sqlboot.util.template_engine.impl;

import com.github.mgramin.sqlboot.util.template_engine.ITemplateEngine;
import groovy.text.GStringTemplateEngine;

public class GroovyTemplateEngine extends AbstractGroovyTemplateEngine implements ITemplateEngine {

    public GroovyTemplateEngine() {
        engine = new GStringTemplateEngine();
    }

    public GroovyTemplateEngine(String template) {
        this();
        setTemplate(template);
    }

    @Override
    public String toString() {
        return "GroovyTemplateEngine{" +
            "template=" + template +
            '}';
    }

}
