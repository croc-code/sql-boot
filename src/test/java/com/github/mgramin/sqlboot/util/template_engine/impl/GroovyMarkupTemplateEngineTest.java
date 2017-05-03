package com.github.mgramin.sqlboot.util.template_engine.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import com.github.mgramin.sqlboot.template_engine.impl.GroovyMarkupTemplateEngine;
import org.junit.Test;

/**
 * Created by mgramin on 06.01.2017.
 */
public class GroovyMarkupTemplateEngineTest {

    @Test
    public void processXml() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("val", "foo");
        assertEquals("<value>foo</value>",
                new GroovyMarkupTemplateEngine("value(val)").process(variables));
    }

    @Test
    public void processHtml() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("border_val", "1");
        assertEquals("<table border='1'><caption>\"HR\".\"USERS\"</caption><tr/></table>",
                new GroovyMarkupTemplateEngine("table(border:border_val){caption('\"HR\".\"USERS\"')tr()}")
                    .process(variables));
    }

}