package com.github.mgramin.sqlboot.util.template_engine;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.util.template_engine.impl.GroovyMarkupTemplateEngine;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by mgramin on 06.01.2017.
 */
public class GroovyMarkupTemplateEngineTest {

    @Test
    public void process() throws Exception {
        ITemplateEngine templateEngine = new GroovyMarkupTemplateEngine();

        Map<String, Object> variables = new HashMap<>();
        variables.put("val", "foo");
        assertEquals("<value>foo</value>", templateEngine.process(variables, "value(val)"));
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void getAllProperties() throws Exception {
        expectedEx.expect(SqlBootException.class);
        expectedEx.expectMessage("Not implemented!");
        new GroovyMarkupTemplateEngine().getAllProperties(null);
    }

}