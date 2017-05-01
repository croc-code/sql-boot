package com.github.mgramin.sqlboot.actions.generator.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by maksim on 08.04.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class SqlWrapperTest {

    /*@Autowired
    ISqlHelper sqlHelper;*/

    @Test
    public void testGenerate() throws Exception {
        /*SqlWrapper sqlGenerator = new SqlWrapper(sqlHelper, new GroovyTemplateEngine(), Collections.singletonList("select 'Hello, ${name}!'"));
        Map variables = new HashMap<String, String>();
        variables.put("name", "World");
        assertEquals(sqlGenerator.generate(variables), "Hello, World!");*/
    }


}