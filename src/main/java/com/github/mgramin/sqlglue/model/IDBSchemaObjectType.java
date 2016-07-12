package com.github.mgramin.sqlglue.model;

import java.util.List;
import java.util.Map;

/**
 * Created by maksim on 08.03.16.
 */
public interface IDBSchemaObjectType {

    Map<String, DBSchemaObject> scan(List<String> list, String action, Boolean recursive);

    String getName();

    void setName(String name);

}