package com.github.mgramin.sqlboot.actions.generator.wrappers;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by MGramin on 04.05.2017.
 */
public class CacheWrapperTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void generate() throws Exception {
        exception.expect(SqlBootException.class);
        exception.expectMessage("Coming soon!");
        new CacheWrapper().generate(null);
    }

}