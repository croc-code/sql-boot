package com.github.mgramin.sqlboot.tools.jdbc.impl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObject;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import java.util.List;
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
public class ForeignKeyTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void name() throws Exception {
        assertEquals("fk", new ForeignKey(dataSource).name());
    }

    @Test
    public void read() throws Exception {
        JdbcDbObjectType foreignKey = new ForeignKey(dataSource);
        List<JdbcDbObject> list = foreignKey.read(asList("MAIN_SCHEMA", "USERS"));
        System.out.println(list);
    }

}