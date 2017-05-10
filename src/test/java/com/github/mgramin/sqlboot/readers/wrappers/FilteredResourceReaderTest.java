/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 mgramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.github.mgramin.sqlboot.readers.wrappers;

import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.model.DbResourceThin;
import com.github.mgramin.sqlboot.model.DbResourceType;
import com.github.mgramin.sqlboot.model.DbUri;
import com.github.mgramin.sqlboot.readers.DbResourceReader;
import java.util.List;
import org.junit.Test;

import java.util.Map;
import java.util.Properties;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Created by maksim on 09.05.17.
 */
public class FilteredResourceReaderTest {

    @Test
    public void read() throws Exception {

        Properties properties = new Properties();
        properties.setProperty("creator", "admin");

        Properties properties2 = new Properties();
        properties2.setProperty("creator", "jdoe");

        DbResourceReader reader = mock(DbResourceReader.class);
        when(reader.read(any(), any())).thenReturn(
            asList(new DbResourceThin("persons", new DbResourceType(new String[]{"table"}, null, null), new DbUri("table/hr.persons?@creator=admin"), properties),
                new DbResourceThin("jobs", new DbResourceType(new String[]{"table"}, null, null), new DbUri("table/hr.jobs?@creator=jdoe"), properties)));

        DbResourceReader filteredReader = new FilteredResourceReader(reader);
        List<DbResource> read = filteredReader.read(new DbUri("table/hr.*?@creator=admin"),
            new DbResourceType(new String[]{"table"}, null, null));

        System.out.println(read);
    }

}