package com.github.mgramin.sqlboot.model;

import com.github.mgramin.sqlboot.uri.DbUri;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mgramin on 20.12.2016.
 */
public class DbResourceTest {

    @Test
    public void toStringTest() throws Exception {
        DbResource tablePersons = new DbResource("persons", new DbResourceType("table"), new DbUri("table/hr.persons"), null);
        assertEquals("DbResource(name=persons, type=DbResourceType(name=table, aliases=null, description=null, child=null, readers=null, aggregators=null), dbUri=table/hr.persons, headers=null, body=null)",
            tablePersons.toString());
    }

}