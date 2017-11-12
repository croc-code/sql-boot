package com.github.mgramin.sqlboot.model.resource_type.wrappers.list;

import static org.junit.Assert.assertEquals;

import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.FakeDbResourceType;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import org.junit.Test;

public class WhereWrapperTest {

    final ResourceType type = new WhereWrapper(new FakeDbResourceType());

    @Test
    public void name() throws Exception {
        assertEquals("fake_resource_type", type.name());
    }

    @Test
    public void aliases() throws Exception {
        assertEquals("[fake_resource_type, fake_type, frt, f]",
            type.aliases().toString());
    }

    @Test
    public void read() throws Exception {
        assertEquals(1, type.read(new DbUri("table/hr.persons")).count());
        assertEquals(3, type.read(new DbUri("table/hr.s")).count());
    }

}