package com.github.mgramin.sqlboot.uri;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by maksim on 12.06.16.
 */
public class ObjURITest {

    @Test
    public void testToString1() throws Exception {
        test("column/hr.persons.%/drop/?key=value&key2=val2",
                "ObjURI{type='column', action='drop', objects=[hr, persons, %], recursive=true, params={key=value, key2=val2}}");
    }

    @Test
    public void testToString2() throws Exception {
        test("column/hr.persons.%/drop?key=value",
                "ObjURI{type='column', action='drop', objects=[hr, persons, %], recursive=false, params={key=value}}");
    }

    @Test
    public void testToString3() throws Exception {
        test("column/hr.persons.%/drop",
                "ObjURI{type='column', action='drop', objects=[hr, persons, %], recursive=false, params={}}");
    }

    @Test
    public void testToString4() throws Exception {
        test("column/hr.persons.%/",
                "ObjURI{type='column', action='create', objects=[hr, persons, %], recursive=true, params={}}");
    }

    @Test
    public void testToString5() throws Exception {
        test("fk/hr.employees/",
                "ObjURI{type='fk', action='create', objects=[hr, employees], recursive=true, params={}}");
    }


    private void test(String uriStringActual, String jsonExpected) {
        ObjURI uri = new ObjURI(uriStringActual);
        assertEquals(uriStringActual, uri.toString());
        assertEquals(uri.toJson(), jsonExpected);
    }

}