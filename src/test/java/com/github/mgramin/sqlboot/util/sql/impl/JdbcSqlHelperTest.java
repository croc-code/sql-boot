package com.github.mgramin.sqlboot.util.sql.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by mgramin on 24.10.2016.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class JdbcSqlHelperTest {

    @Autowired
    private JdbcSqlHelper jdbcSqlHelper;

    @Test
    public void select() throws Exception {
        List<Map<String, String>> select = jdbcSqlHelper.select("select * from (select name AS n, email as mail from main_schema.users)");
        assertEquals(select.toString(), "[{N=mkyong, MAIL=mkyong@gmail.com}, {N=alex, MAIL=alex@yahoo.com}, {N=joel, MAIL=joel@gmail.com}]");
    }

    @Test
    public void selectBatch() throws Exception {
        List<Map<String, String>> select = jdbcSqlHelper.selectBatch(
                Arrays.asList(
                        "create table test_temp_table (id integer)",
                        "SeLeCt * from (select name AS n, email as mail from main_schema.users)",
                        "sElEcT * from (select name AS n, email as mail from main_schema.users)"));
        assertEquals(select.toString(), "[{N=mkyong, MAIL=mkyong@gmail.com}, {N=alex, MAIL=alex@yahoo.com}, {N=joel, MAIL=joel@gmail.com}, {N=mkyong, MAIL=mkyong@gmail.com}, {N=alex, MAIL=alex@yahoo.com}, {N=joel, MAIL=joel@gmail.com}]");
    }

}