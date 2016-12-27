package com.github.mgramin.sqlboot.actions.generator.impl;

import com.github.mgramin.sqlboot.util.template_engine.impl.FMTemplateEngine;
import org.junit.Ignore;
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
 * Created by mgramin on 29.10.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class StoredFunctionGeneratorTest {

    @Autowired
    DataSource dataSource;

    @Test
    public void generate() throws Exception {
        StoredFunctionGenerator actionGenerator = new StoredFunctionGenerator();
        actionGenerator.setDataSource(dataSource);
        actionGenerator.setTemplateEngine(new FMTemplateEngine());
        actionGenerator.setFunctionName("getVersion");

        Map map = new HashMap<String, String>();
        map.put("name", "World");

        assertEquals(actionGenerator.generate(map), "1.4.187");
    }

}