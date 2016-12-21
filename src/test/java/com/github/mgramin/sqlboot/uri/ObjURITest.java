package com.github.mgramin.sqlboot.uri;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by maksim on 12.06.16.
 */
public class ObjURITest {

    @Test
    public void createAllTableFromSchema() {
        test("table/hr.*", "ObjURI{type='table', action='create', objects=[hr, *], recursive=false, params={}}");
    }

    @Test
    public void createAllTableWithChildObjectsFromSchema() {
        test("table/hr.*/", "ObjURI{type='table', action='create', objects=[hr, *], recursive=true, params={}}");
    }

    @Test
    public void dropAllTableFromSchema() {
        test("table/hr.*/drop", "ObjURI{type='table', action='drop', objects=[hr, *], recursive=false, params={}}");
    }


    @Test
    public void createColumnsForTable() {
        test("column/hr.persons.*name",
                "ObjURI{type='column', action='create', objects=[hr, persons, *name], recursive=false, params={}}");
    }

    @Test
    public void dropColumnFromTable() {
        test("column/hr.persons.name/drop",
                "ObjURI{type='column', action='drop', objects=[hr, persons, name], recursive=false, params={}}");
    }


    @Test
    public void createAllFkForTable() {
        test("fk/hr.employees.*",
                "ObjURI{type='fk', action='create', objects=[hr, employees, *], recursive=false, params={}}");
    }

    @Test
    public void dropAllFkFromTable() {
        test("fk/hr.employees.*/drop",
                "ObjURI{type='fk', action='drop', objects=[hr, employees, *], recursive=false, params={}}");
    }

    @Test
    public void disableAllFkFromTable() {
        test("fk/hr.employees.*/disable",
                "ObjURI{type='fk', action='disable', objects=[hr, employees, *], recursive=false, params={}}");
    }

    @Test
    public void disableAllFkFromSchema() {
        test("fk/hr.*.*/disable",
                "ObjURI{type='fk', action='disable', objects=[hr, *, *], recursive=false, params={}}");
    }

    @Test
    public void testDefaultActionIsCreate() {
        test("fk/hr.*.*",
                "ObjURI{type='fk', action='create', objects=[hr, *, *], recursive=false, params={}}");
    }

    private void test(String uriStringActual, String jsonExpected) {
        ObjURI uri = new ObjURI(uriStringActual);
        assertEquals(uriStringActual, uri.toString());
        assertEquals(uri.toJson(), jsonExpected);
    }

}