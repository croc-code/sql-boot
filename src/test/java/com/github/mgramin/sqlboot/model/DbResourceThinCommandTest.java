package com.github.mgramin.sqlboot.model;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * Created by MGramin on 05.05.2017.
 */
public class DbResourceThinCommandTest {

    @Test
    public void name() throws Exception {
        IDbResourceCommand command = new DbResourceCommand(new String[] {"create", "c", "+"});
        assertEquals("create", command.name());
    }

    @Test
    public void aliases() throws Exception {
        IDbResourceCommand command = new DbResourceCommand(new String[] {"create", "c", "+"});
        assertEquals(asList("create", "c", "+"), command.aliases());
    }

    @Test
    public void isDefault() throws Exception {
        assertEquals(false,
            new DbResourceCommand(new String[] {"create", "c", "+"}).isDefault());
         assertEquals(true,
            new DbResourceCommand(new String[] {"create", "c", "+"}, true).isDefault());
    }

    @Test
    public void toStringTest() throws Exception {
        IDbResourceCommand command = new DbResourceCommand(new String[] {"create", "c", "+"});
        assertEquals("DbResourceCommand(aliases=[create, c, +], isDefault=false)", command.toString());
    }

}