package com.github.mgramin.sqlglue.actions.generator.impl;

import com.github.mgramin.sqlglue.actions.generator.AbstractActionGenerator;
import com.github.mgramin.sqlglue.util.template_engine.ITemplateEngine;

import java.util.Map;

/**
 * Created by maksim on 05.04.16.
 */
public class TemplateGenerator extends AbstractActionGenerator {

    private ITemplateEngine templateEngine;
    private Map<String, Object> variables;
    private String template;

    public TemplateGenerator() {
    }

    public TemplateGenerator(ITemplateEngine templateEngine, Map<String, Object> variables, String template) {
        this.templateEngine = templateEngine;
        this.variables = variables;
        this.template = template;
    }

    @Override
    public String generate(Map<String, Object> variables) {
        String result = templateEngine.process(variables, template);
        // Arrays.asList(result.split("\n")).forEach(n -> n.trim());
        return result;
    }


    public ITemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
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
