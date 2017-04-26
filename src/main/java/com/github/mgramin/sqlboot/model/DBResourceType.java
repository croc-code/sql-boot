package com.github.mgramin.sqlboot.model;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import com.github.mgramin.sqlboot.readers.IDBObjectReader;
import lombok.ToString;

import java.util.List;

/**
 * Resource type of DB
 * e.g. "table", "index", "pk", "stored procedure", "session", "block" etc
 */
@ToString
public class DBResourceType {

    public String name;
    public List<String> aliases;
    public String description;
    public List<DBResourceType> child;
    public List<IDBObjectReader> readers;
    public List<DBSchemaObjectTypeAggregator> aggregators;

    /*Map<String, DBSchemaObject> read(ObjURI objURI) throws SqlBootException {
        return null;
    }*/

    public DBResourceType() {
    }

    public DBResourceType(String name, List<DBResourceType> child, List<IDBObjectReader> readers) {
        this.name = name;
        this.child = child;
        this.readers = readers;
    }

    public DBResourceType(String name, List<IDBObjectReader> reader) {
        this.name = name;
        this.readers = reader;
    }

    public DBResourceType(String name, IDBObjectReader reader) {
        this.name = name;
        this.readers = singletonList(reader);
    }

    public DBResourceType(String name, List<DBResourceType> child, IDBObjectReader reader) {
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

    public void setChild(List<DBResourceType> child) {
        this.child = child;
    }

    public void setReaders(List<IDBObjectReader> readers) {
        this.readers = readers;
    }

    public void setAggregators(List<DBSchemaObjectTypeAggregator> aggregators) {
        this.aggregators = aggregators;
    }

    public void setAliases(String[] aliases) {
        this.aliases = asList(aliases);
    }

}