package com.github.mgramin.sqlboot.actions.generator.wrappers;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;

import java.util.Map;

/**
 * Created by maksim on 29.04.17.
 */
public class CacheWrapper implements IActionGenerator {

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        throw new RuntimeException("Coming soon!");
    }

    @Override
    public DBSchemaObjectCommand command() {
        throw new RuntimeException("Coming soon!");
    }

}
