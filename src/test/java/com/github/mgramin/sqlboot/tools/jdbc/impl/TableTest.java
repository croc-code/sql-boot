package com.github.mgramin.sqlboot.tools.jdbc.impl;

import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObject;
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
        List<JdbcDbObject> list = table.read(Arrays.asList("%", "%"));

        System.out.println(list);
    }

}