package com.github.mgramin.sqlboot.actions.generator;

import com.github.mgramin.sqlboot.actions.Action;
import com.github.mgramin.sqlboot.actions.generator.impl.SQLGenerator;
import org.apache.log4j.Logger;

/**
 * Created by maksim on 23.05.16.
 */
public abstract class AbstractActionGenerator implements IActionGenerator {

    protected final static Logger logger = Logger.getLogger(SQLGenerator.class);

    public String filePath;
    public Action action;


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

}