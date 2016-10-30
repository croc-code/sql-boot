package com.github.mgramin.sqlglue.model;

import com.github.mgramin.sqlglue.actions.generator.IActionGenerator;

import java.util.List;
import java.util.Map;

/**
 * Created by maksim on 08.03.16.
 */
public interface IDBSchemaObjectType {

    Map<String, DBSchemaObject> scan(List<String> list, String action, Boolean recursive);

    List<IActionGenerator> getCommands();

    String getName();

    void setName(String name);

}