package com.github.mgramin.sqlboot.model;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.readers.IDBObjectReader;

import java.util.List;

/**
 * Created by maksim on 19.05.16.
 */
public class DBSchemaObjectType {

    public String name;
    public String description;
    public List<DBSchemaObjectType> child;
    public List<IDBObjectReader> readers;
    public List<IActionGenerator> commands;

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChild(List<DBSchemaObjectType> child) {
        this.child = child;
    }

    public void setCommands(List<IActionGenerator> commands) {
        this.commands = commands;
    }

    public void setReaders(List<IDBObjectReader> readers) {
        this.readers = readers;
    }

    @Override
    public String toString() {
        return "DBSchemaObjectType{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}