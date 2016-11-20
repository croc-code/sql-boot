package com.github.mgramin.sqlglue.util.sql;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by mgramin on 24.10.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class JdbcSqlHelperTest {

    @Autowired
    JdbcSqlHelper jdbcSqlHelper;

    @Test
    public void select() throws Exception {
        List<Map<String, String>> select = jdbcSqlHelper.select("select * from (select name AS n, email as mail from main_schema.users)");
        assertEquals(select.toString(), "[{N=mkyong, MAIL=mkyong@gmail.com}, {N=alex, MAIL=alex@yahoo.com}, {N=joel, MAIL=joel@gmail.com}]");
    }

}