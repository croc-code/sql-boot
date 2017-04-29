package com.github.mgramin.sqlboot.actions.generator.wrappers;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;

import java.util.Map;

/**
 * Created by maksim on 29.04.17.
 */
public class LogWrapper implements IActionGenerator {

    public LogWrapper(IActionGenerator baseGenerator) {
        this.baseGenerator = baseGenerator;
    }

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        // TODO logging before
        return baseGenerator.generate(variables);
        // TODO logging after
    }

    @Override
    public DBSchemaObjectCommand command() {
        return baseGenerator.command();
    }

    private final IActionGenerator baseGenerator;

}
