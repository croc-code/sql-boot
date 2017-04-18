package com.github.mgramin.sqlboot.actions.generator.impl;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.actions.generator.wrappers.TemplateGenerator;
import com.github.mgramin.sqlboot.util.template_engine.impl.GroovyTemplateEngine;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by maksim on 08.04.16.
 */
public class TemplateGeneratorTest {

    @Test
    public void testGenerate() throws Exception {
        Map<String, Object> maps = new HashMap<>();
        maps.put("name", "World");
        IActionGenerator commandGenerator = new TemplateGenerator(new GroovyTemplateEngine(), "Hello, ${name}!", null);
        assertEquals(commandGenerator.generate(maps), "Hello, World!");
    }
}