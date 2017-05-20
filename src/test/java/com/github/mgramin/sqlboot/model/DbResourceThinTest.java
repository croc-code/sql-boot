package com.github.mgramin.sqlboot.model;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by mgramin on 20.12.2016.
 */
public class DbResourceThinTest {

    @Test
    @Ignore
    public void toStringTest() throws Exception {
        DbResource tablePersons = new DbResourceThin("persons", new DbResourceType(new String[]{"table"}, null, null, null), new DbUri("table/hr.persons"), null);
        assertEquals("DbResourceThin(name=persons, type=DbResourceType(aliases=[table], child=null, readers=null, aggregators=null), dbUri=table/hr.persons, headers=null)",
            tablePersons.toString());
    }

}