package com.github.mgramin.sqlglue.actions;

import java.util.List;

/**
 * Created by maksim on 21.06.16.
 */
public class ActionContainer {

    private List<Action> actions;

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public Action getActionByName(String name) {
        // actions.stream().filter(line -> name.equals(line))
        return null;
    }

}
