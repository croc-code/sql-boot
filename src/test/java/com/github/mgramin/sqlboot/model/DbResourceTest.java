package com.github.mgramin.sqlboot.model;

import com.github.mgramin.sqlboot.uri.ObjUri;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mgramin on 20.12.2016.
 */
public class DbResourceTest {

    @Test
    public void toStringTest() throws Exception {
        DbResource tablePersons = new DbResource("persons", new DbResourceType("table"), new ObjUri("table/hr.persons"), null, null);
        assertEquals("DbResource(name=persons, type=DbResourceType(name=table, aliases=null, description=null, child=null, readers=null, aggregators=null), objUri=table/hr.persons, headers=null, body=null, paths=null)",
            tablePersons.toString());
    }

    @Test
    public void compareTo() throws Exception {
        DbResource tablePersons = new DbResource("persons", new DbResourceType("table"), new ObjUri("table/hr.persons"), null, null);
        DbResource tableJobs = new DbResource("jobs", new DbResourceType("table"), new ObjUri("table/hr.jobs"), null, null);
        assertEquals(-6, tableJobs.compareTo(tablePersons));
    }

}