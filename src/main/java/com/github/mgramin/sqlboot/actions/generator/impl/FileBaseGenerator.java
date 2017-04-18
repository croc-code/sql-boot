package com.github.mgramin.sqlboot.actions.generator.impl;

import com.github.mgramin.sqlboot.actions.generator.AbstractActionGenerator;
import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by maksim on 18.04.17.
 */
public class FileBaseGenerator extends AbstractActionGenerator implements IActionGenerator {

    public FileBaseGenerator(Resource file) {
        this.file = file;
    }

    private final Resource file;

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        try {
            return new String(Files.readAllBytes(Paths.get(file.getFile().getName())), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new SqlBootException(e);
        }
    }

}
