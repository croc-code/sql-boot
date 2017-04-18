package com.github.mgramin.sqlboot.actions.generator.wrappers;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;
import com.github.mgramin.sqlboot.util.template_engine.impl.GroovyTemplateEngine;

import java.util.Map;

/**
 * Created by maksim on 19.04.17.
 */
public class GroovyTemplateWrapper implements IActionGenerator {

    public GroovyTemplateWrapper(IActionGenerator actionGenerator) {
        this.actionGenerator = actionGenerator;
    }

    final private IActionGenerator actionGenerator;

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        GroovyTemplateEngine groovyTemplateEngine = new GroovyTemplateEngine(actionGenerator.generate(variables));
        return groovyTemplateEngine.process(variables);
    }

    @Override
    public DBSchemaObjectCommand command() {
        return null;
    }

}
