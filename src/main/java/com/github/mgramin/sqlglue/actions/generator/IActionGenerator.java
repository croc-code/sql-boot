package com.github.mgramin.sqlglue.actions.generator;

import com.github.mgramin.sqlglue.actions.Action;

import java.util.Map;

/**
 * Created by maksim on 05.04.16.
 */
public interface IActionGenerator {

    String generate(Map<String, Object> variables);

    String getFilePath();

    void setFilePath(String filePath);

    Action getAction();

    void setAction(Action action);

}