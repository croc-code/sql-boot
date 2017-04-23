package com.github.mgramin.sqlboot.actions.generator.impl;

import com.github.mgramin.sqlboot.actions.generator.AbstractActionGenerator;
import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;

import java.util.Map;

/**
 * Created by maksim on 18.04.17.
 */
public class PlainTextGenerator extends AbstractActionGenerator implements IActionGenerator {

    public PlainTextGenerator(String baseText, DBSchemaObjectCommand command) {
        this.baseText = baseText;
        this.dbSchemaObjectCommand = command;
    }

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        return baseText;
    }

    private final String baseText;

}
