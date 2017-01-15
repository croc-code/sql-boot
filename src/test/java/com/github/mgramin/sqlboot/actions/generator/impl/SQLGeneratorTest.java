package com.github.mgramin.sqlboot.actions.generator.impl;

import com.github.mgramin.sqlboot.util.sql.ISqlHelper;
import com.github.mgramin.sqlboot.util.template_engine.impl.FMTemplateEngine;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by maksim on 08.04.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class SQLGeneratorTest {

    @Autowired
    ISqlHelper sqlHelper;

    @Test
    public void testGenerate() throws Exception {
        SQLGenerator sqlGenerator = new SQLGenerator(sqlHelper, new FMTemplateEngine(), Collections.singletonList("select 'Hello, ${name}!'"));
        Map variables = new HashMap<String, String>();
        variables.put("name", "World");
        assertEquals(sqlGenerator.generate(variables), "Hello, World!");
    }

}