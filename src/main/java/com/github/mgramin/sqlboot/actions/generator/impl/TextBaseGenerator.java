package com.github.mgramin.sqlboot.actions.generator.impl;

import com.github.mgramin.sqlboot.actions.generator.AbstractActionGenerator;
import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;

import java.util.Map;

/**
 * Created by maksim on 18.04.17.
 */
public class TextBaseGenerator extends AbstractActionGenerator implements IActionGenerator {

    public TextBaseGenerator(String baseText) {
        this.baseText = baseText;
    }

    private final String baseText;

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        return baseText;
    }

}
