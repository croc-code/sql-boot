package com.github.mgramin.sqlboot.model;

import java.util.Arrays;
import java.util.List;

/**
 * Command for db-object, e.g. "create", "drop", "exists", "rebuild", "gather"(statistics),
 * "compile"(procedure, function, package), etc
 */
public class DBSchemaObjectCommand {

    public String name;
    public List<String> aliases;
    public Boolean isDefault;


    public void setName(String name) {
        this.name = name;
    }

    public void setAliases(String[] aliases) {
        this.aliases = Arrays.asList(aliases);
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