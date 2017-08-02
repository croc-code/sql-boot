package com.github.mgramin.sqlboot.resource_type.wrappers.list;

import static org.junit.Assert.assertEquals;

import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.resource_type.ResourceType;
import com.github.mgramin.sqlboot.resource_type.impl.FakeDbResourceType;
import com.github.mgramin.sqlboot.uri.impl.DbUri;
import com.github.mgramin.sqlboot.uri.impl.FakeUri;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

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