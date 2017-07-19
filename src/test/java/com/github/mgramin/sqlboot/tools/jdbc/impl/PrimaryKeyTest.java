package com.github.mgramin.sqlboot.tools.jdbc.impl;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by MGramin on 17.07.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class PrimaryKeyTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void name() throws Exception {
        assertEquals("pk", new PrimaryKey(dataSource).name());
    }

    @Test
    public void read() throws Exception {
        JdbcDbObjectType primaryKey = new PrimaryKey(dataSource);
        List<Map<String, String>> maps = primaryKey.read(asList("MAIN_SCH%", "USERS"));
        System.out.println(maps);
    }

}