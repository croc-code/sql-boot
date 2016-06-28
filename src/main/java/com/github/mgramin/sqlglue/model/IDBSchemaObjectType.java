package com.github.mgramin.sqlglue.model;

import java.util.List;

/**
 * Created by maksim on 08.03.16.
 */
public interface IDBSchemaObjectType {

    List<DBSchemaObject> scan(List<String> list, Boolean recursive, String action);

    String getName();

    void setName(String name);

}