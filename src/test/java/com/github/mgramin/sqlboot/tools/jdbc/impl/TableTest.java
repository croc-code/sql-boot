package com.github.mgramin.sqlboot.tools.jdbc.impl;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Set;
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
        Set<String> properties = new Table(dataSource).read().stream().findAny().get().properties().keySet();
        assertThat(properties, hasItems("TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME"));
    }

}