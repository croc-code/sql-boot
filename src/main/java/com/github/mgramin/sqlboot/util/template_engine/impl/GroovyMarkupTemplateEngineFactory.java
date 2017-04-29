package com.github.mgramin.sqlboot.util.template_engine.impl;

import com.github.mgramin.sqlboot.util.template_engine.TemplateEngine;
import com.github.mgramin.sqlboot.util.template_engine.TemplateEngineFactory;

/**
 * Created by maksim on 29.04.17.
 */
public class GroovyMarkupTemplateEngineFactory implements TemplateEngineFactory {

    @Override
    public TemplateEngine create(String template) {
        return new GroovyMarkupTemplateEngine(template);
    }

}
