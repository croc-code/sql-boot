package com.github.mgramin.sqlglue.actions;

/**
 * Created by maksim on 19.06.16.
 */
public class Action {

    public String name;
    public String aliases;


    public void setName(String name) {
        this.name = name;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }


    @Override
    public String toString() {
        return "Action{" +
                "name='" + name + '\'' +
                ", aliases='" + aliases + '\'' +
                '}';
    }

}