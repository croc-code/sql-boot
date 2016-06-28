package com.github.mgramin.sqlglue.model;

import java.util.Properties;

/**
 * Created by maksim on 19.05.16.
 */
public class DBSchemaObject {

    private String name;
    private IDBSchemaObjectType type;
    private String ddl;
    private Properties properties = new Properties();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IDBSchemaObjectType getType() {
        return type;
    }

    public void setType(IDBSchemaObjectType type) {
        this.type = type;
    }

    public String getDdl() {
        return ddl;
    }

    public void setDdl(String ddl) {
        this.ddl = ddl;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void addProperty(Properties properties){

    }

    @Override
    public String toString() {
        return "DBSchemaObject{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", ddl='" + ddl + '\'' +
                ", properties=" + properties +
                '}';
    }

}