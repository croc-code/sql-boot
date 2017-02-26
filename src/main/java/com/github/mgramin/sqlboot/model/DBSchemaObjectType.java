package com.github.mgramin.sqlboot.model;

import static java.util.Collections.singletonList;

import com.github.mgramin.sqlboot.readers.IDBObjectReader;
import java.util.Arrays;
import java.util.List;

/**
 * Object type of DB schema
 * e.g. "table", "index", "pk", "stored procedure" etc
 */
public class DBSchemaObjectType {

    public String name;
    public List<String> aliases;
    public String description;
    public List<DBSchemaObjectType> child;
    public List<IDBObjectReader> readers;
    public List<DBSchemaObjectTypeAggregator> aggregators;

    /*Map<String, DBSchemaObject> read(ObjURI objURI) throws SqlBootException {
        return null;
    }*/

    public DBSchemaObjectType() {
    }

    public DBSchemaObjectType(String name, List<DBSchemaObjectType> child, List<IDBObjectReader> readers) {
        this.name = name;
        this.child = child;
        this.readers = readers;
    }

    public DBSchemaObjectType(String name, List<IDBObjectReader> reader) {
        this.name = name;
        this.readers = reader;
    }

    public DBSchemaObjectType(String name, IDBObjectReader reader) {
        this.name = name;
        this.readers = singletonList(reader);
    }

    public DBSchemaObjectType(String name, List<DBSchemaObjectType> child, IDBObjectReader reader) {
        this.name = name;
        this.child = child;
        this.readers = singletonList(reader);
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChild(List<DBSchemaObjectType> child) {
        this.child = child;
    }

    public void setReaders(List<IDBObjectReader> readers) {
        this.readers = readers;
    }

    public void setAggregators(List<DBSchemaObjectTypeAggregator> aggregators) {
        this.aggregators = aggregators;
    }

    public void setAliases(String[] aliases) {
        this.aliases = Arrays.asList(aliases);
    }

    @Override
    public String toString() {
        return "DBSchemaObjectType{" +
                "name='" + name + '\'' +
                ", aliases=" + aliases +
                ", description='" + description + '\'' +
                '}';
    }
}