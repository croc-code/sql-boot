package com.github.mgramin.sqlboot.tools.jdbc.impl;

import java.util.Map;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObject;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
    @Ignore
    public void read() throws Exception {
        final JdbcDbObjectType column = new Column(dataSource);
        final JdbcDbObject jdbcDbObject = column.read(asList("MAIN_SCHEMA", "CITY", "ID")).stream()
            .findAny()
            .get();
        final Map<String, String> prop = jdbcDbObject.properties();

        assertEquals("ID", jdbcDbObject.name());
        assertThat(prop, hasEntry("TABLE_SCHEMA", "MAIN_SCHEMA"));
        assertThat(prop, hasEntry("TABLE_NAME", "CITY"));
        assertThat(prop, hasEntry("COLUMN_NAME", "ID"));
        assertThat(prop, hasEntry("CHARACTER_OCTET_LENGTH", "10"));
        assertThat(prop, hasEntry("IS_AUTOINCREMENT", "NO"));
        assertThat(prop, hasEntry("ORDINAL_POSITION", "1"));
        assertThat(prop, hasEntry("TYPE_NAME", "INTEGER"));
    }

}