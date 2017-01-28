package com.github.mgramin.sqlboot.uri;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by maksim on 12.06.16.
 */
public class ObjURITest {

    @Test
    public void createAllTableFromSchema() throws SqlBootException {
        test("table/hr.*", "ObjURI{type='table', DBSchemaObjectCommand='null', objects=[hr, *], recursive=false, params={}}");
    }

    @Test
    public void createAllTableWithChildObjectsFromSchema() throws SqlBootException {
        test("table/hr.*/", "ObjURI{type='table', DBSchemaObjectCommand='null', objects=[hr, *], recursive=true, params={}}");
    }

    @Test
    public void dropAllTableFromSchema() throws SqlBootException {
        test("table/hr.*/drop", "ObjURI{type='table', DBSchemaObjectCommand='drop', objects=[hr, *], recursive=false, params={}}");
    }


    @Test
    public void createColumnsForTable() throws SqlBootException {
        test("column/hr.persons.*name",
                "ObjURI{type='column', DBSchemaObjectCommand='null', objects=[hr, persons, *name], recursive=false, params={}}");
    }

    @Test
    public void dropColumnFromTable() throws SqlBootException {
        test("column/hr.persons.name/drop",
                "ObjURI{type='column', DBSchemaObjectCommand='drop', objects=[hr, persons, name], recursive=false, params={}}");
    }


    @Test
    public void createAllFkForTable() throws SqlBootException {
        test("fk/hr.employees.*",
                "ObjURI{type='fk', DBSchemaObjectCommand='null', objects=[hr, employees, *], recursive=false, params={}}");
    }

    @Test
    public void dropAllFkFromTable() throws SqlBootException {
        test("fk/hr.employees.*/drop",
                "ObjURI{type='fk', DBSchemaObjectCommand='drop', objects=[hr, employees, *], recursive=false, params={}}");
    }

    @Test
    public void disableAllFkFromTable() throws SqlBootException {
        test("fk/hr.employees.*/disable",
                "ObjURI{type='fk', DBSchemaObjectCommand='disable', objects=[hr, employees, *], recursive=false, params={}}");
    }

    @Test
    public void disableAllFkFromSchema() throws SqlBootException {
        test("fk/hr.*.*/disable",
                "ObjURI{type='fk', DBSchemaObjectCommand='disable', objects=[hr, *, *], recursive=false, params={}}");
    }

    @Test
    public void testDefaultActionIsCreate() throws SqlBootException {
        test("fk/hr.*.*",
                "ObjURI{type='fk', DBSchemaObjectCommand='null', objects=[hr, *, *], recursive=false, params={}}");
    }


    @Test
    public void testParams() throws SqlBootException, URISyntaxException {
        test("t/hr?@table_comment=big_table",
            "ObjURI{type='t', DBSchemaObjectCommand='null', objects=[hr], recursive=false, params={@table_comment=big_table}}");
        test("table/hr?@table_comment=big_table",
            "ObjURI{type='table', DBSchemaObjectCommand='null', objects=[hr], recursive=false, params={@table_comment=big_table}}");
    }


    private void test(String uriStringActual, String jsonExpected) throws SqlBootException {
        ObjURI uri = new ObjURI(uriStringActual);
        assertEquals(uriStringActual, uri.toString());
        assertEquals(uri.toJson(), jsonExpected);
    }

}