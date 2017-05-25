/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Maksim Gramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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