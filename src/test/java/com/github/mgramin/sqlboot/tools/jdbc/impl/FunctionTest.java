package com.github.mgramin.sqlboot.tools.jdbc.impl;

import javax.sql.DataSource;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

/**
 * Created by MGramin on 13.07.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class FunctionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void name() throws Exception {
        assertEquals("function", new Function(dataSource).name());
    }

    @Test
    public void read() throws Exception {
        JdbcDbObjectType function = new Function(dataSource);
        /*List<Map<String, String>> maps = function.read(Arrays.asList("%", "%"));
        System.out.println(maps);*/
    }

}