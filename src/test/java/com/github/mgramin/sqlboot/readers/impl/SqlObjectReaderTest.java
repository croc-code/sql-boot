package com.github.mgramin.sqlboot.readers.impl;

import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.model.DbResourceType;
import com.github.mgramin.sqlboot.model.DbUri;
import com.github.mgramin.sqlboot.readers.DbResourceReader;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mgramin on 31.10.2016.
 */
public class SqlObjectReaderTest {

    @Test
    public void readRecursive() throws Exception {
        DbUri uri = new DbUri("column/hr.persons.*");

        ISqlHelper sqlHelper = mock(ISqlHelper.class);
        when(sqlHelper.select(any())).thenReturn(asList(
                of("schema", "public", "table", "persons", "column", "id"),
                of("schema", "public", "table", "persons", "column", "name"),
                of("schema", "public", "table", "persons", "column", "age")));

        ActionGenerator actionGenerator = mock(ActionGenerator.class);
        when(actionGenerator.generate(any(List.class))).thenReturn("alter table $table_name add column $column_name ...");

        SqlResourceReader reader = new SqlResourceReader(sqlHelper, actionGenerator);
        DbResourceType column = new DbResourceType(new String[]{"column"}, null, asList(reader), null);

        assertEquals(
                reader.read(uri, column).keySet(),
                Sets.newHashSet("column/public.persons.id", "column/public.persons.name", "column/public.persons.age"));
    }

    @Test
    public void readRecursiveWithChildType() throws Exception {
        DbUri uri = new DbUri("table/hr.*");

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


        DbResourceType index = new DbResourceType(new String[]{"index"}, null, asList(readerIndex), null);
        DbResourceType table = new DbResourceType(new String[]{"table"}, asList(index), asList(readerTable), null);

        assertEquals(
                readerTable.readr(uri, table).keySet(),
                Sets.newHashSet("table/public.persons", "index/public.persons.persons_idx"));
    }

}