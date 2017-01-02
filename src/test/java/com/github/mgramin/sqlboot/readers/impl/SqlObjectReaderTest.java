package com.github.mgramin.sqlboot.readers.impl;

import com.github.mgramin.sqlboot.model.DBSchemaObjectType;
import com.github.mgramin.sqlboot.uri.ObjURI;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;
import com.github.mgramin.sqlboot.util.template_engine.ITemplateEngine;
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
        ObjURI uri = new ObjURI("column/hr.persons.%");

        ISqlHelper sqlHelper = mock(ISqlHelper.class);
        when(sqlHelper.select(any())).thenReturn(asList(
                of("schema", "public", "table", "persons", "column", "id"),
                of("schema", "public", "table", "persons", "column", "name"),
                of("schema", "public", "table", "persons", "column", "age")));

        ITemplateEngine templateEngine = mock(ITemplateEngine.class);
        when(templateEngine.getAllProperties(any())).thenReturn(asList("@schema", "@table", "@column"));

        SqlObjectReader reader = new SqlObjectReader(sqlHelper, templateEngine,
                "... custom-sql for select objects from db dictionary ...",
                "... execute before custom-sql in same session ...");

        DBSchemaObjectType column = new DBSchemaObjectType("column", reader);

        assertEquals(
                reader.read(uri, column).keySet(),
                Sets.newHashSet("column/public.persons.id", "column/public.persons.name", "column/public.persons.age"));
    }

    @Test
    public void readRecursiveWithChildType() throws Exception {
        ObjURI uri = new ObjURI("table/hr.*");

        ISqlHelper sqlHelperTableMock = mock(ISqlHelper.class);
        when(sqlHelperTableMock.select(any())).thenReturn(asList(
                of("schema", "public", "table", "persons")));
        ITemplateEngine templateEngineTableMock = mock(ITemplateEngine.class);
        when(templateEngineTableMock.getAllProperties(any())).thenReturn(asList("@schema", "@table"));

        SqlObjectReader readerTable = new SqlObjectReader(sqlHelperTableMock, templateEngineTableMock,
                "... custom-sql for select table from db dictionary ...");


        ISqlHelper sqlHelperIndexMock = mock(ISqlHelper.class);
        when(sqlHelperIndexMock.select(any())).thenReturn(asList(
                of("schema", "public", "table", "persons", "index", "persons_idx")));
        ITemplateEngine templateEngineIndexMock = mock(ITemplateEngine.class);
        when(templateEngineIndexMock.getAllProperties(any())).thenReturn(asList("@schema", "@table", "@index"));

        SqlObjectReader readerIndex = new SqlObjectReader(sqlHelperIndexMock, templateEngineIndexMock,
                "... custom-sql for select index from db dictionary ...");


        DBSchemaObjectType index = new DBSchemaObjectType("index", readerIndex);
        DBSchemaObjectType table = new DBSchemaObjectType("table", Arrays.asList(index), readerTable);

        assertEquals(
                readerTable.readr(uri, table).keySet(),
                Sets.newHashSet("table/public.persons", "index/public.persons.persons_idx"));
    }

}