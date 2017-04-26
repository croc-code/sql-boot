package com.github.mgramin.sqlboot.model;

import com.github.mgramin.sqlboot.uri.ObjURI;
import lombok.ToString;

import java.util.Map;
import java.util.Properties;

/**
 * DB Resource
 * e.g. table "PERSONS", index "PERSONS_NAME_IDX", stored function "GET_ALL_DEPARTMENTS()" etc
 */
@ToString
public class DBResource implements Comparable<DBResource> {

    public String name;
    public DBResourceType type;
    public ObjURI objURI;
    public Properties headers = new Properties();
    public String body;
    @Deprecated
    public Map<String, String> paths;

    public String getProp(String key) {
        return headers.getProperty(key);
    }

    public void addProperty(Object key, Object value){
        this.headers.put(key, value);
    }

    public String getName() {
        return name;
    }

    public DBResourceType getType() {
        return type;
    }

    public String getBody() {
        return body;
    }

    public ObjURI getObjURI() {
        return objURI;
    }

    public Map<String, String> getPaths() {
        return paths;
    }

    public Properties getHeaders() {
        return headers;
    }

    @Override
    public int compareTo(DBResource o) {
        return (this.objURI.toString()).compareTo(o.objURI.toString());
    }

}