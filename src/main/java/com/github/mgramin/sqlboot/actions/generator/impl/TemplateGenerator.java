package com.github.mgramin.sqlboot.actions.generator.impl;

import com.github.mgramin.sqlboot.actions.generator.AbstractActionGenerator;
import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;
import com.github.mgramin.sqlboot.util.template_engine.ITemplateEngine;

import java.util.Map;

public class TemplateGenerator extends AbstractActionGenerator implements IActionGenerator {

    public TemplateGenerator(ITemplateEngine templateEngine, String template, DBSchemaObjectCommand command) {
        this.templateEngine = templateEngine;
        this.templateEngine.setTemplate(template);
        this.dbSchemaObjectCommand = command;
    }

    private ITemplateEngine templateEngine;

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        return templateEngine.process(variables);
    }


    @Override
    public String toString() {
        return "TemplateGenerator{" +
                "template='" + templateEngine + '\'' +
                '}';
    }

}