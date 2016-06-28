package com.github.mgramin.sqlglue.actions;

/**
 * Created by maksim on 19.06.16.
 */
public class Action {

    private String name;
    private String aliases;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliases() {
        return aliases;
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