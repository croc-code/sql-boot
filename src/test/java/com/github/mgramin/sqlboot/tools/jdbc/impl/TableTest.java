package com.github.mgramin.sqlboot.tools.jdbc.impl;

import static org.junit.Assert.assertEquals;

import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by MGramin on 13.07.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class TableTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void name() throws Exception {
        assertEquals("table", new Table(dataSource).name());
    }

    @Test
    public void read() throws Exception {
        JdbcDbObjectType table = new Table(dataSource);
        List<Map<String, String>> maps = table.read(Arrays.asList("%", "%"));

        System.out.println(maps);
    }

}