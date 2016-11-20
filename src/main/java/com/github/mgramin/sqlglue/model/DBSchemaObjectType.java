package com.github.mgramin.sqlglue.model;

import com.github.mgramin.sqlglue.actions.generator.IActionGenerator;
import com.github.mgramin.sqlglue.scanners.IObjectScanner;

import java.util.List;

/**
 * Created by maksim on 19.05.16.
 */
public class DBSchemaObjectType {

    public String name;
    public String description;
    public List<DBSchemaObjectType> child;
    public List<IObjectScanner> scanners;
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

    public void setScanners(List<IObjectScanner> scanners) {
        this.scanners = scanners;
    }

    @Override
    public String toString() {
        return "DBSchemaObjectType{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}