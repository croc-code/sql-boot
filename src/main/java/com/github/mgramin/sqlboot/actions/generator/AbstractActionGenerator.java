package com.github.mgramin.sqlboot.actions.generator;

import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;
import com.github.mgramin.sqlboot.actions.generator.impl.SQLGenerator;
import org.apache.log4j.Logger;

/**
 * Created by maksim on 23.05.16.
 */
public abstract class AbstractActionGenerator implements IActionGenerator {

    protected final static Logger logger = Logger.getLogger(SQLGenerator.class);

    public DBSchemaObjectCommand DBSchemaObjectCommand;


    public DBSchemaObjectCommand getDBSchemaObjectCommand() {
        return DBSchemaObjectCommand;
    }

    public void setDBSchemaObjectCommand(DBSchemaObjectCommand DBSchemaObjectCommand) {
        this.DBSchemaObjectCommand = DBSchemaObjectCommand;
    }

}