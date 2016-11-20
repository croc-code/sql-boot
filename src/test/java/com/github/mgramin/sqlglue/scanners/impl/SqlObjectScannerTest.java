package com.github.mgramin.sqlglue.scanners.impl;

import com.github.mgramin.sqlglue.actions.generator.impl.TemplateGenerator;
import com.github.mgramin.sqlglue.model.DBSchemaObjectType;
import com.github.mgramin.sqlglue.uri.ObjURI;
import com.github.mgramin.sqlglue.util.sql.ISqlHelper;
import com.github.mgramin.sqlglue.util.template_engine.ITemplateEngine;
import com.github.mgramin.sqlglue.util.template_engine.impl.FMTemplateEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mgramin on 31.10.2016.
 */
/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})*/
public class SqlObjectScannerTest {

    @Test
    public void scan() throws Exception {
        ObjURI uri = new ObjURI("idx/hr.persons.%/drop");

        ISqlHelper sqlHelper = mock(ISqlHelper.class);
        when(sqlHelper.select(any())).thenReturn(asList(
                of("schema", "public", "table", "persons", "column", "id"),
                of("schema", "public", "table", "persons", "column", "name"),
                of("schema", "public", "table", "persons", "column", "age")));

        ITemplateEngine templateEngine = mock(ITemplateEngine.class);
        when(templateEngine.getAllProperties(any())).thenReturn(asList("@schema", "@table", "@column"));

        SqlObjectScanner scanner = new SqlObjectScanner(sqlHelper, templateEngine, "", "");

        DBSchemaObjectType columnType = new DBSchemaObjectType();
        columnType.setName("column");
        columnType.setDescription("column of table");
        columnType.setCommands(Arrays.asList(
                new TemplateGenerator(new FMTemplateEngine(), "alter table table ${schema}.${table} drop column ${column};")));
        columnType.setScanners(Arrays.asList(scanner));
        columnType.setChild(Arrays.asList());

        scanner.scan(uri, columnType);
    }


}