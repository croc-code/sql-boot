package com.github.mgramin.sqlboot.actions.generator;

import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;

import java.util.Map;

/**
 * Created by maksim on 05.04.16.
 */
public interface IActionGenerator {

    String generate(Map<String, Object> variables) throws SqlBootException;


    DBSchemaObjectCommand getDBSchemaObjectCommand();

    void setDBSchemaObjectCommand(DBSchemaObjectCommand DBSchemaObjectCommand);

}