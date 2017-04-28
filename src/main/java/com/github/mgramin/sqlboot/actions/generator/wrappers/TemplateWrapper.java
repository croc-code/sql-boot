package com.github.mgramin.sqlboot.actions.generator.wrappers;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;
import com.github.mgramin.sqlboot.util.template_engine.TemplateEngine;

import java.util.Map;

/**
 * Created by maksim on 19.04.17.
 */
public class TemplateWrapper implements IActionGenerator {

    public TemplateWrapper(IActionGenerator baseGenerator, TemplateEngine templateEngine) {
        this.baseGenerator = baseGenerator;
        this.templateEngine = templateEngine;
    }

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        templateEngine.setTemplate(baseGenerator.generate(variables));
        return templateEngine.process(variables);
    }

    @Override
    public DBSchemaObjectCommand command() {
        return baseGenerator.command();
    }

    final private IActionGenerator baseGenerator;
    final private TemplateEngine templateEngine;

}
