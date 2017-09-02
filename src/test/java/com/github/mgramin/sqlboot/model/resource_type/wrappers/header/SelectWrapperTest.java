package com.github.mgramin.sqlboot.model.resource_type.wrappers.header;

import static org.junit.Assert.assertEquals;

import com.github.mgramin.sqlboot.model.resource_type.impl.FakeDbResourceType;
import com.github.mgramin.sqlboot.model.uri.impl.FakeUri;
import org.junit.Test;

public class SelectWrapperTest {

    final SelectWrapper type = new SelectWrapper(new FakeDbResourceType());

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
        assertEquals(3, type.read(new FakeUri()).size());
    }

}