package com.github.mgramin.sqlboot.actions.generator.prepared;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.actions.generator.impl.PlainTextGenerator;
import com.github.mgramin.sqlboot.actions.generator.wrappers.SQLWrapper;
import com.github.mgramin.sqlboot.actions.generator.wrappers.TemplateWrapper;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;
import com.github.mgramin.sqlboot.util.template_engine.TemplateEngine;

import java.util.Map;

/**
 * Created by maksim on 29.04.17.
 */
public class PlainTextTemplateSqlGenerator implements IActionGenerator {

    public PlainTextTemplateSqlGenerator(String baseText, DBSchemaObjectCommand command, TemplateEngine templateEngine,
                                         ISqlHelper sqlHelper) {
        baseGenerator =
                new SQLWrapper(
                    new TemplateWrapper(
                        new PlainTextGenerator(baseText, command),
                        templateEngine),
                    sqlHelper);

    }

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        return baseGenerator.generate(variables);
    }

    @Override
    public DBSchemaObjectCommand command() {
        return baseGenerator.command();
    }

    private final IActionGenerator baseGenerator;

}
