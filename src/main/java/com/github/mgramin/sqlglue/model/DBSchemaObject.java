package com.github.mgramin.sqlglue.model;

import com.github.mgramin.sqlglue.uri.ObjURI;

import java.util.Map;
import java.util.Properties;

/**
 * Created by maksim on 19.05.16.
 */
public class DBSchemaObject implements Comparable<DBSchemaObject> {

    public String name;
    public DBSchemaObjectType type;
    public String ddl;
    public ObjURI objURI;
    public Map<String, String> paths;
    public Properties properties = new Properties();

    public String getProp(String key) {
        return properties.getProperty(key);
    }

    public void addProperty(Object key, Object value){
        this.properties.put(key, value);
    }

    @Override
    public String toString() {
        return "DBSchemaObject{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", ddl='" + ddl + '\'' +
                ", objURI=" + objURI +
                ", paths=" + paths +
                ", properties=" + properties +
                '}';
    }

    public String getName() {
        return name;
    }

    public DBSchemaObjectType getType() {
        return type;
    }

    public String getDdl() {
        return ddl;
    }

    public ObjURI getObjURI() {
        return objURI;
    }

    public Map<String, String> getPaths() {
        return paths;
    }

    public Properties getProperties() {
        return properties;
    }

    @Override
    public int compareTo(DBSchemaObject o) {
        return (this.objURI.toString()).compareTo(o.objURI.toString());
    }

}