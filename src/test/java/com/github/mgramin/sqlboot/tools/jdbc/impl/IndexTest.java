package com.github.mgramin.sqlboot.tools.jdbc.impl;

import java.util.List;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObject;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

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
        JdbcDbObjectType index = new Index(dataSource);
        List<JdbcDbObject> list = index.read(asList("MAIN_SCHEMA", "USERS"));
        System.out.println(list);
    }

}