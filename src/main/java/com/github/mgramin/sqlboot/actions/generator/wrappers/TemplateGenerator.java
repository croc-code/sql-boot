package com.github.mgramin.sqlboot.actions.generator.wrappers;

import com.github.mgramin.sqlboot.actions.generator.AbstractActionGenerator;
import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;
import com.github.mgramin.sqlboot.util.template_engine.TemplateEngine;

import java.util.Map;
import lombok.ToString;

@Deprecated
@ToString
public class TemplateGenerator extends AbstractActionGenerator implements IActionGenerator {

    public TemplateGenerator(TemplateEngine templateEngine, String template, DBSchemaObjectCommand command) {
        this.templateEngine = templateEngine;
        this.templateEngine.setTemplate(template);
        this.dbSchemaObjectCommand = command;
    }

    private TemplateEngine templateEngine;

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        return templateEngine.process(variables);
    }

}