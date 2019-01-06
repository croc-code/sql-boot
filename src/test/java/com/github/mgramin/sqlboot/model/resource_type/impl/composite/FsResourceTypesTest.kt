/*
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2019 Maksim Gramin
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.model.resource_type.impl.composite

import org.junit.Assert.assertEquals

import com.github.mgramin.sqlboot.model.resource.DbResource
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import java.util.stream.Stream
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.core.io.FileSystemResource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 5231745cc5b9b08cedd762394593203cd13cbd25 $
 * @since 0.1
 */
@RunWith(SpringRunner::class)
@ContextConfiguration(locations = arrayOf("/test_config.xml"))
class FsResourceTypesTest {

    // TODO use parametrized tests ?

    @Test
    fun test() {

    }

    /*@Test
    public void loadFromSql() throws Exception {
        final FsResourceTypes types = new FsResourceTypes(new FileSystemResource("conf/h2/database"));
        types.setUrl("jdbc:h2:mem:;INIT=RUNSCRIPT FROM 'classpath:schema.sql';");
        types.init();
        final Stream<DbResource> dbResourceStream = types.read(new DbUri("table/bookings.airports"));

        final DbResource dbResource = dbResourceStream.findFirst().get();
        System.out.println(dbResource.name());
        System.out.println(dbResource.body());
        assertEquals(1, dbResourceStream.count());

        assertEquals(5, types.read(new DbUri("table/bookings")).count());
    }

    @Test
    public void loadFromJdbc() throws Exception {
        final FsResourceTypes types = new FsResourceTypes(new FileSystemResource("conf/common/database"));
        types.setUrl("jdbc:h2:mem:;INIT=RUNSCRIPT FROM 'classpath:schema.sql';");
        types.init();
        assertEquals(1, types.read(new DbUri("table/bookings.airports")).count());
        assertEquals(5, types.read(new DbUri("table/bookings")).count());
    }*/

}