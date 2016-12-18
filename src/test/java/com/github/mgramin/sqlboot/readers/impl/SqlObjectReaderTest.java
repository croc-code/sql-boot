package com.github.mgramin.sqlboot.readers.impl;

import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.model.DBSchemaObjectType;
import com.github.mgramin.sqlboot.uri.ObjURI;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;
import com.github.mgramin.sqlboot.util.template_engine.ITemplateEngine;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

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
    public void read() throws Exception {
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

        DBSchemaObjectType column = new DBSchemaObjectType();
        column.setName("column");
        column.setDescription("column of simple relational table");
        column.setReaders(Arrays.asList(reader));

        Map<String, DBSchemaObject> objects = reader.read(uri, column);

        for (Map.Entry<String, DBSchemaObject> stringDBSchemaObjectEntry : objects.entrySet()) {
            System.out.println(stringDBSchemaObjectEntry.getValue());
        }
    }

}