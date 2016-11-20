package com.github.mgramin.sqlglue.actions.generator.impl;

import com.github.mgramin.sqlglue.actions.generator.AbstractActionGenerator;
import com.github.mgramin.sqlglue.actions.generator.IActionGenerator;
import com.github.mgramin.sqlglue.exceptions.GlueException;
import com.github.mgramin.sqlglue.util.template_engine.ITemplateEngine;

import java.util.Map;

/**
 * Created by maksim on 05.04.16.
 */
public class TemplateGenerator extends AbstractActionGenerator implements IActionGenerator {

    public ITemplateEngine templateEngine;
    public String template;

    public TemplateGenerator(ITemplateEngine templateEngine, String template) {
        this.templateEngine = templateEngine;
        this.template = template;
    }

    @Override
    public String generate(Map<String, Object> variables) throws GlueException {
        return templateEngine.process(variables, template);
    }


    public void setTemplateEngine(ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public TemplateGenerator() {
    }

    @Override
    public String toString() {
        return "TemplateGenerator{" +
                "template='" + template + '\'' +
                '}';
    }

}