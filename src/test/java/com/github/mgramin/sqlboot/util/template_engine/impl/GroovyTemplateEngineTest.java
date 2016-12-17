package com.github.mgramin.sqlboot.util.template_engine.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.util.template_engine.ITemplateEngine;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by MGramin on 10.12.2016.
 */
public class GroovyTemplateEngineTest {

    @Test
    public void process() throws SqlBootException {
        ITemplateEngine templateEngine = new GroovyTemplateEngine();

        String txt = "... where lower(c.table_schema) like '$schema'\n" +
                "and lower(c.table_name) like '$table'\n" +
                "and lower(c.column_name) like '$column'";

        String result = "... where lower(c.table_schema) like 'public'\n" +
                "and lower(c.table_name) like 'persons'\n" +
                "and lower(c.column_name) like 'id'";

        Map<String, Object> maps = new HashMap<>();
        maps.put("column", "id");
        maps.put("table", "persons");
        maps.put("schema", "public");

        assertEquals(templateEngine.process(maps, txt), result);
    }

    @Test
    public void processLoweCase() throws SqlBootException {
    ITemplateEngine templateEngine = new GroovyTemplateEngine();

        Map<String, Object> maps = new HashMap<>();
        maps.put("column", "id");
        maps.put("table", "persons");
        maps.put("schema", "public");

        assertEquals(templateEngine.process(maps, "create table !{table.toLowerCase()} ..."), "create table persons ...");
    }

    @Test
    public void getAllProperties() throws SqlBootException {
        ITemplateEngine templateEngine = new GroovyTemplateEngine();
        String txt = "... where lower(c.table_schema) like '$schema'\n" +
                "and lower(c.table_name) like '$table'\n" +
                "and lower(c.column_name) like '$column'";
        assertEquals(templateEngine.getAllProperties(txt), Arrays.asList("schema", "table", "column"));

        assertEquals(templateEngine.getAllProperties("drop table $schema.$table;"), Arrays.asList("schema", "table"));
    }

}