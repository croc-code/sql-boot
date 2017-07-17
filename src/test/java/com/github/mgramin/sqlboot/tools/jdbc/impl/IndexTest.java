package com.github.mgramin.sqlboot.tools.jdbc.impl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import com.github.mgramin.sqlboot.tools.jdbc.DbObject;
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
public class IndexTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void name() throws Exception {
        assertEquals("index", new Index(dataSource).name());
    }

    @Test
    public void read() throws Exception {
        DbObject index = new Index(dataSource);
        List<Map<String, String>> maps = index.read(asList("MAIN_SCHEMA", "USERS"));
        System.out.println(maps);
    }

}