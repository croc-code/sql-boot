package com.github.mgramin.sqlboot.model.resource_type.wrappers.list;

import static org.junit.Assert.assertEquals;

import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.FakeDbResourceType;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import java.util.stream.Stream;
import org.junit.Test;

public class PageWrapperTest {

    final ResourceType type = new PageWrapper(new FakeDbResourceType());

    @Test
    public void name() {
    }

    @Test
    public void aliases() {
    }

    @Test
    public void path() {
    }

    @Test
    public void metaData() {
    }

    @Test
    public void read() {
        assertEquals("persons", type.read(new DbUri("table/hr?page=1:1")).findAny().get().name());
        assertEquals("users", type.read(new DbUri("table/hr?page=2:1")).findAny().get().name());
        assertEquals("jobs", type.read(new DbUri("table/hr?page=3:1")).findAny().get().name());
        assertEquals(1, type.read(new DbUri("table/hr?page=1:1")).count());
        assertEquals(2, type.read(new DbUri("table/hr?page=1:2")).count());
        assertEquals(3, type.read(new DbUri("table/hr?page=1:3")).count());
        assertEquals(3, type.read(new DbUri("table/hr?page=1")).count());
        assertEquals(0, type.read(new DbUri("table/hr?page=2")).count());
    }

}