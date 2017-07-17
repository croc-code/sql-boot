package com.github.mgramin.sqlboot.tools.jdbc.impl;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import com.github.mgramin.sqlboot.tools.jdbc.DbObject;
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
 * Created by MGramin on 17.07.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class ColumnTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void name() throws Exception {
        assertEquals("column", new Column(dataSource).name());
    }

    @Test
    public void read() throws Exception {
        DbObject column = new Column(dataSource);
        List<Map<String, String>> maps = column.read(asList("%", "%", "%"));
        System.out.println(maps);
    }

}