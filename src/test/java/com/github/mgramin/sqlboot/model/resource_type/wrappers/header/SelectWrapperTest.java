package com.github.mgramin.sqlboot.model.resource_type.wrappers.header;

import static org.junit.Assert.assertEquals;

import com.github.mgramin.sqlboot.model.resource.DbResource;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.model.resource_type.impl.FakeDbResourceType;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import java.util.List;
import org.junit.Test;

public class SelectWrapperTest {

    final ResourceType type = new SelectWrapper(new FakeDbResourceType());

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
        final List<DbResource> resources = type.read(new DbUri("table/hr.persons?select=schema"));
        for (DbResource resource : resources) {
            assertEquals(1, resource.headers().size());
        }
        assertEquals(3, resources.size());
    }

}