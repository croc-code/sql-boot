package com.github.mgramin.sqlboot.model.uri.wrappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import org.junit.Test;

public class JsonWrapperTest {

    final Uri uri = new JsonWrapper(
        new DbUri("table/hr.*persons*/"));

    @Test
    public void type() throws Exception {
        assertEquals("table", uri.type());
    }

    @Test
    public void path() throws Exception {
        assertEquals("%persons%", uri.path().get(1));
    }

    @Test
    public void recursive() throws Exception {
        assertTrue(uri.recursive());
    }

    @Test
    public void params() throws Exception {
        assertEquals(0, uri.params().size());
    }

    @Test
    public void toStringTest() throws Exception {
        assertEquals("DbUri{type='table', path=[hr, *persons*], recursive=true, params={}}",
            uri.toString());
    }

}