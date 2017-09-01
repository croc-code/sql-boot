package com.github.mgramin.sqlboot.model.resource_type.wrappers.header;

import com.github.mgramin.sqlboot.model.resource_type.impl.FakeDbResourceType;
import com.github.mgramin.sqlboot.model.uri.impl.FakeUri;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SelectWrapperTest {

    @Test
    public void name() throws Exception {
        assertEquals("fake_resource_type", new SelectWrapper(new FakeDbResourceType()).name());
    }

    @Test
    public void aliases() throws Exception {
    }

    @Test
    public void read() throws Exception {
        final SelectWrapper type = new SelectWrapper(new FakeDbResourceType());
        assertEquals(3, type.read(new FakeUri()).size());
    }

}