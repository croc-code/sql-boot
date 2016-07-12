package com.github.mgramin.sqlglue.model;

import com.github.mgramin.sqlglue.uri.ObjURI;

import java.util.Map;
import java.util.Properties;

/**
 * Created by maksim on 19.05.16.
 */
public class DBSchemaObject implements Comparable<DBSchemaObject> {

    private String name;
    private IDBSchemaObjectType type;
    private String ddl;

    private ObjURI objURI;
    private Map<String, String> paths;

    private Properties properties = new Properties();


    public String getProperty(String key) {
        return properties.getProperty(key);
    }


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


    @Override
    public int compareTo(DBSchemaObject o) {
        return (this.name).compareTo(o.name);
    }

    public ObjURI getObjURI() {
        return objURI;
    }

    public void setObjURI(ObjURI objURI) {
        this.objURI = objURI;
    }

    public Map<String, String> getPaths() {
        return paths;
    }

    public void setPaths(Map<String, String> paths) {
        this.paths = paths;
    }
}