package com.github.mgramin.sqlboot.model;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;

import java.util.List;

/**
 * Created by MGramin on 09.01.2017.
 */
public class DBSchemaObjectTypeAggregator {

    private String aggregatorName;
    private List<IActionGenerator> commands;


    public String getAggregatorName() {
        return aggregatorName;
    }

    public void setAggregatorName(String aggregatorName) {
        this.aggregatorName = aggregatorName;
    }

    public List<IActionGenerator> getCommands() {
        return commands;
    }

    public void setCommands(List<IActionGenerator> commands) {
        this.commands = commands;
    }
}