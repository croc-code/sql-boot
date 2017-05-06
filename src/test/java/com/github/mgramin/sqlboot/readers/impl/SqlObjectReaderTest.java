package com.github.mgramin.sqlboot.readers.impl;

import com.github.mgramin.sqlboot.model.DbResourceType;
import com.github.mgramin.sqlboot.uri.DbUri;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;
import com.github.mgramin.sqlboot.template_engine.TemplateEngine;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Arrays;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

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

        TemplateEngine templateEngine = mock(TemplateEngine.class);
        when(templateEngine.getAllProperties()).thenReturn(asList("@schema", "@table", "@column"));

        SqlResourceReader reader = new SqlResourceReader(sqlHelper, templateEngine,
                "... custom-sql for select objects from db dictionary ...");

        DbResourceType column = new DbResourceType("column", reader);

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
        TemplateEngine templateEngineTableMock = mock(TemplateEngine.class);
        when(templateEngineTableMock.getAllProperties()).thenReturn(asList("@schema", "@table"));

        SqlResourceReader readerTable = new SqlResourceReader(sqlHelperTableMock, templateEngineTableMock,
                "... custom-sql for select table from db dictionary ...");


        ISqlHelper sqlHelperIndexMock = mock(ISqlHelper.class);
        when(sqlHelperIndexMock.select(any())).thenReturn(asList(
                of("schema", "public", "table", "persons", "index", "persons_idx")));
        TemplateEngine templateEngineIndexMock = mock(TemplateEngine.class);
        when(templateEngineIndexMock.getAllProperties()).thenReturn(asList("@schema", "@table", "@index"));

        SqlResourceReader readerIndex = new SqlResourceReader(sqlHelperIndexMock, templateEngineIndexMock,
                "... custom-sql for select index from db dictionary ...");


        DbResourceType index = new DbResourceType("index", readerIndex);
        DbResourceType table = new DbResourceType("table", Arrays.asList(index), readerTable);

        assertEquals(
                readerTable.readr(uri, table).keySet(),
                Sets.newHashSet("table/public.persons", "index/public.persons.persons_idx"));
    }

}