package com.github.mgramin.sqlboot.actions.generator.impl;

import com.github.mgramin.sqlboot.util.template_engine.impl.FMTemplateEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
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
    DataSource dataSource;

    @Test
    public void testGenerate() throws Exception {
        SQLGenerator sqlGenerator = new SQLGenerator(dataSource, new FMTemplateEngine(), "select 'Hello, ${name}!'");
        Map map = new HashMap<String, String>();
        map.put("name", "World");
        assertEquals(sqlGenerator.generate(map), "Hello, World!");
    }

}