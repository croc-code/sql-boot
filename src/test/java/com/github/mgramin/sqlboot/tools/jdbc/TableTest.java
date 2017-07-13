package com.github.mgramin.sqlboot.tools.jdbc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
    public void get() throws Exception {

        List<Map<String, String>> maps;

        /*DbObject table = new Table(dataSource);
        maps = table.get(Arrays.asList("%", "%"));

        for (Map<String, String> map : maps) {
            System.out.println();
            for (Entry<String, String> stringStringEntry : map.entrySet()) {
                System.out.println(stringStringEntry.getKey() + " = " + stringStringEntry.getValue());
            }
        }*/

        DbObject column = new Column(dataSource);
        maps = column.get(Arrays.asList("%", "%", "%"));

        for (Map<String, String> map : maps) {
            System.out.println();
            for (Entry<String, String> stringStringEntry : map.entrySet()) {
                System.out.println(stringStringEntry.getKey() + " = " + stringStringEntry.getValue());
            }
        }

    }

}