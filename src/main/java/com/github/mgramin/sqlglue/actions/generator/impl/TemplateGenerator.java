package com.github.mgramin.sqlglue.actions.generator.impl;

import com.github.mgramin.sqlglue.actions.generator.AbstractActionGenerator;
import com.github.mgramin.sqlglue.util.template_engine.ITemplateEngine;

import java.util.Map;

/**
 * Created by maksim on 05.04.16.
 */
public class TemplateGenerator extends AbstractActionGenerator {

    private ITemplateEngine templateEngine;
    private String template;

    public TemplateGenerator() {
    }

    public TemplateGenerator(ITemplateEngine templateEngine, String template) {
        this.templateEngine = templateEngine;
        this.template = template;
    }

    @Override
    public String generate(Map<String, Object> variables) {
        String result = templateEngine.process(variables, template);
        return result;
    }


    public ITemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }


    @Override
    public String toString() {
        return "TemplateGenerator{" +
                "template='" + template + '\'' +
                '}';
    }

}
