package com.github.mgramin.sqlboot.model;

import lombok.ToString;

import java.util.Arrays;
import java.util.List;

/**
 * Command for db-object, e.g. "create", "drop", "exists", "rebuild", "gather"(statistics),
 * "compile"(procedure, function, package), etc
 */
@ToString
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

}