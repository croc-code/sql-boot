package com.github.mgramin.sqlboot.uri;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import java.net.URISyntaxException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by maksim on 12.06.16.
 */
public class ObjUriTest {

    @Test
    public void createAllTableFromAllSchema() {
        test("table/*", "ObjUri{type='table', dbSchemaObjectCommand='null', objects=[*], recursive=false, params={}}");
    }

    @Test
    public void createAllTableFromSchema() {
        test("table/hr", "ObjUri{type='table', dbSchemaObjectCommand='null', objects=[hr], recursive=false, params={}}");
        test("table/hr.*", "ObjUri{type='table', dbSchemaObjectCommand='null', objects=[hr, *], recursive=false, params={}}");
    }

    @Test
    public void createAllTableWithChildObjectsFromSchema() throws SqlBootException {
        test("table/hr.*/", "ObjUri{type='table', dbSchemaObjectCommand='null', objects=[hr, *], recursive=true, params={}}");
    }

    @Test
    public void dropAllTableFromSchema() throws SqlBootException {
        test("table/hr.*/drop", "ObjUri{type='table', dbSchemaObjectCommand='drop', objects=[hr, *], recursive=false, params={}}");
    }


    @Test
    public void createColumnsForTable() throws SqlBootException {
        test("column/hr.persons.*name",
                "ObjUri{type='column', dbSchemaObjectCommand='null', objects=[hr, persons, *name], recursive=false, params={}}");
    }

    @Test
    public void dropColumnFromTable() throws SqlBootException {
        test("column/hr.persons.name/drop",
                "ObjUri{type='column', dbSchemaObjectCommand='drop', objects=[hr, persons, name], recursive=false, params={}}");
    }


    @Test
    public void createAllFkForTable() throws SqlBootException {
        test("fk/hr.employees.*",
                "ObjUri{type='fk', dbSchemaObjectCommand='null', objects=[hr, employees, *], recursive=false, params={}}");
    }

    @Test
    public void dropAllFkFromTable() throws SqlBootException {
        test("fk/hr.employees.*/drop",
                "ObjUri{type='fk', dbSchemaObjectCommand='drop', objects=[hr, employees, *], recursive=false, params={}}");
    }

    @Test
    public void disableAllFkFromTable() throws SqlBootException {
        test("fk/hr.employees.*/disable",
                "ObjUri{type='fk', dbSchemaObjectCommand='disable', objects=[hr, employees, *], recursive=false, params={}}");
    }

    @Test
    public void disableAllFkFromSchema() throws SqlBootException {
        test("fk/hr.*.*/disable",
                "ObjUri{type='fk', dbSchemaObjectCommand='disable', objects=[hr, *, *], recursive=false, params={}}");
    }

    @Test
    public void testDefaultActionIsCreate() throws SqlBootException {
        test("fk/hr.*.*",
                "ObjUri{type='fk', dbSchemaObjectCommand='null', objects=[hr, *, *], recursive=false, params={}}");
    }


    @Test
    public void testParams() throws SqlBootException, URISyntaxException {
        test("t/hr?@table_comment=big_table",
            "ObjUri{type='t', dbSchemaObjectCommand='null', objects=[hr], recursive=false, params={@table_comment=big_table}}");
        test("table/hr?@table_comment=big_table",
            "ObjUri{type='table', dbSchemaObjectCommand='null', objects=[hr], recursive=false, params={@table_comment=big_table}}");
    }


    private void test(String uriStringActual, String jsonExpected) throws SqlBootException {
        ObjUri uri = new ObjUri(uriStringActual);
        assertEquals(uriStringActual, uri.toString());
        assertEquals(uri.toJson(), jsonExpected);
    }

}