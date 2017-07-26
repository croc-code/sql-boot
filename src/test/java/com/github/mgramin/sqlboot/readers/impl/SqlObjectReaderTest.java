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

package com.github.mgramin.sqlboot.readers.impl;

import com.github.mgramin.sqlboot.resource_type.impl.ResourceType;
import com.github.mgramin.sqlboot.readers.impl.sql.SqlResourceReader;
import java.util.List;
import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.uri.impl.DbUri;
import com.github.mgramin.sqlboot.uri.Uri;
import com.github.mgramin.sqlboot.readers.DbResourceReader;
import com.github.mgramin.sqlboot.sql.ISqlHelper;
import org.junit.Test;
import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mgramin on 31.10.2016.
 */
public class SqlObjectReaderTest {

    @Test
    public void readRecursive() throws Exception {
        Uri uri = new DbUri("column/hr.persons.*");

        ISqlHelper sqlHelper = mock(ISqlHelper.class);
        when(sqlHelper.select(any())).thenReturn(asList(
                of("schema", "public", "table", "persons", "column", "id"),
                of("schema", "public", "table", "persons", "column", "name"),
                of("schema", "public", "table", "persons", "column", "age")));

        ActionGenerator actionGenerator = mock(ActionGenerator.class);
        when(actionGenerator.generate(any(List.class))).thenReturn("alter table $table_name add column $column_name ...");

        SqlResourceReader reader = new SqlResourceReader(sqlHelper, actionGenerator);
        ResourceType column = new ResourceType(new String[]{"column"}, null, asList(reader), null);
    }

    @Test
    public void readRecursiveWithChildType() throws Exception {
        Uri uri = new DbUri("table/hr.*");

        ISqlHelper sqlHelperTableMock = mock(ISqlHelper.class);
        when(sqlHelperTableMock.select(any())).thenReturn(asList(
                of("schema", "public", "table", "persons")));

        ActionGenerator actionGeneratorTableMock = mock(ActionGenerator.class);
        when(actionGeneratorTableMock.generate(any(List.class))).thenReturn("create table $table_name ...");

        DbResourceReader readerTable = new SqlResourceReader(sqlHelperTableMock, actionGeneratorTableMock);


        ISqlHelper sqlHelperIndexMock = mock(ISqlHelper.class);
        when(sqlHelperIndexMock.select(any())).thenReturn(asList(
                of("schema", "public", "table", "persons", "index", "persons_idx")));

        ActionGenerator actionGeneratorIndexMock = mock(ActionGenerator.class);
        when(actionGeneratorIndexMock.generate(any(List.class))).thenReturn("create index ${table_name}.${index_name} ...");

        SqlResourceReader readerIndex = new SqlResourceReader(sqlHelperIndexMock, actionGeneratorIndexMock);


        ResourceType index = new ResourceType(new String[]{"index"}, null, asList(readerIndex), null);
        ResourceType table = new ResourceType(new String[]{"table"}, asList(index), asList(readerTable), null);
    }

}