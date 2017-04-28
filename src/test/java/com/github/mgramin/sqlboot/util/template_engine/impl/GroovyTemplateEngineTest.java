package com.github.mgramin.sqlboot.util.template_engine.impl;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.assertEquals;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.util.template_engine.TemplateEngine;
import java.util.Arrays;
import java.util.Map;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by MGramin on 10.12.2016.
 */
public class GroovyTemplateEngineTest {

    @Test
    public void process() throws SqlBootException {
        String txt = "... where lower(c.table_schema) like '$schema'\n" +
            "and lower(c.table_name) like '$table'\n" +
            "and lower(c.column_name) like '$column'";

        String result = "... where lower(c.table_schema) like 'public'\n" +
            "and lower(c.table_name) like 'persons'\n" +
            "and lower(c.column_name) like 'id'";

        Map<String, Object> maps = of("column", "id", "table", "persons", "schema", "public");
        TemplateEngine templateEngine = new GroovyTemplateEngine(txt);
        assertEquals(templateEngine.process(maps), result);
    }

    @Test
    public void processLoweCase() throws SqlBootException {
        Map<String, Object> maps = of("column", "id", "table", "persons", "schema", "public");
        TemplateEngine templateEngine = new GroovyTemplateEngine("create table !{table.toLowerCase()} ...");
        assertEquals(templateEngine.process(maps), "create table persons ...");
    }

    @Test
    @Ignore
    public void getAllProperties() throws SqlBootException {
        TemplateEngine templateEngine = new GroovyTemplateEngine();
        String txt = "... where lower(c.table_schema) like '$schema'\n" +
            "and lower(c.table_name) like '$table'\n" +
            "and lower(c.column_name) like '$column'";
        assertEquals(templateEngine.getAllProperties(),
            Arrays.asList("schema", "table", "column"));

        assertEquals(templateEngine.getAllProperties(),
            Arrays.asList("schema", "table"));
    }

}