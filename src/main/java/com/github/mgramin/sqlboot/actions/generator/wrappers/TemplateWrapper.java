package com.github.mgramin.sqlboot.actions.generator.wrappers;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;
import com.github.mgramin.sqlboot.util.template_engine.ITemplateEngine;

import java.util.Map;

/**
 * Created by maksim on 19.04.17.
 */
public class TemplateWrapper implements IActionGenerator {

    public TemplateWrapper(IActionGenerator actionGenerator, ITemplateEngine templateEngine) {
        this.actionGenerator = actionGenerator;
        this.templateEngine = templateEngine;
    }

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        return templateEngine.process(variables);
    }

    @Override
    public DBSchemaObjectCommand command() {
        return actionGenerator.command();
    }

    final private IActionGenerator actionGenerator;
    final private ITemplateEngine templateEngine;

}
