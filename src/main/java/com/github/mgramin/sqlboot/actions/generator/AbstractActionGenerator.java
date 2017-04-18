package com.github.mgramin.sqlboot.actions.generator;

import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;

/**
 * Created by maksim on 23.05.16.
 */
public abstract class AbstractActionGenerator implements IActionGenerator {

    protected DBSchemaObjectCommand dbSchemaObjectCommand;

    public DBSchemaObjectCommand command() {
        return dbSchemaObjectCommand;
    }

}