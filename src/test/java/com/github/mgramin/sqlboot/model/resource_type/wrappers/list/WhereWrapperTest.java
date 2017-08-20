package com.github.mgramin.sqlboot.model.resource_type.wrappers.list;

import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.FakeDbResourceType;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WhereWrapperTest {

    @Test
    public void name() throws Exception {
    }

    @Test
    public void aliases() throws Exception {
    }

    @Test
    public void read() throws Exception {
        final ResourceType type = new WhereWrapper(new FakeDbResourceType());
        assertEquals(1, type.read(new DbUri("table/hr.persons")).size());
        assertEquals(3, type.read(new DbUri("table/hr.s")).size());
    }

}