package com.github.mgramin.sqlboot.model;

/**
 * Command for db-object, e.g. "create", "drop", "exists", "rebuild", "gather"(statistics),
 * "compile"(procedure, function, package), etc
 */
public class DBSchemaObjectCommand {

    public String name;
    public String aliases;
    public Boolean isDefault;


    public void setName(String name) {
        this.name = name;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "DBSchemaObjectCommand{" +
                "name='" + name + '\'' +
                ", aliases='" + aliases + '\'' +
                '}';
    }

}