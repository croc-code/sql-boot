package com.github.mgramin.sqlboot.tools.jdbc.impl;

import static org.junit.Assert.assertEquals;

import java.util.Set;
import javax.sql.DataSource;
import org.junit.Ignore;
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
    @Ignore
    public void read() throws Exception {
        Set<String> properties = new Index(dataSource).read().stream().findAny().get().properties().keySet();
        System.out.println(properties);
    }

}