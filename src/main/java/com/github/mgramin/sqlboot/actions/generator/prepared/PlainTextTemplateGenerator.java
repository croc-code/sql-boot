package com.github.mgramin.sqlboot.actions.generator.prepared;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.actions.generator.impl.PlainTextGenerator;
import com.github.mgramin.sqlboot.actions.generator.wrappers.TemplateGeneratorWrapper;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;
import com.github.mgramin.sqlboot.util.template_engine.ITemplateEngine;

import java.util.Map;

/**
 * Created by maksim on 27.04.17.
 */
public class PlainTextTemplateGenerator implements IActionGenerator {

    public PlainTextTemplateGenerator(String baseText, DBSchemaObjectCommand command, ITemplateEngine templateEngine) {
        baseGenerator =
                new TemplateGeneratorWrapper(
                        new PlainTextGenerator(baseText, command),
                        templateEngine);
    }

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        return baseGenerator.generate(variables);
    }

    @Override
    public DBSchemaObjectCommand command() {
        return baseGenerator.command();
    }

    private final TemplateGeneratorWrapper baseGenerator;

}