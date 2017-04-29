package com.github.mgramin.sqlboot.util.template_engine.impl;

import com.github.mgramin.sqlboot.util.template_engine.TemplateEngine;
import groovy.text.GStringTemplateEngine;
import lombok.ToString;

@ToString
public class GroovyTemplateEngine extends AbstractGroovyTemplateEngine implements TemplateEngine {

    @Deprecated
    public GroovyTemplateEngine() {
        engine = new GStringTemplateEngine();
    }

    public GroovyTemplateEngine(String template) {
        this();
        setTemplate(template);
    }

}
