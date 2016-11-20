package sqlglue.uri;

import org.junit.Test;

/**
 * Created by maksim on 12.06.16.
 */
public class ObjURITest {

    @Test
    public void testToString() throws Exception {

        /*assertEquals(new ObjURI("column/hr.persons.%/drop/?key=value&key2=val2").toString(),
                "ObjURI{type='column', action='drop', objects=[hr, persons, %], recursive=true, params={key2=val2, key=value}}");

        assertEquals(
                new ObjURI("column/hr.persons.%/drop?key=value").toString(),
                "ObjURI{type='column', action='drop', objects=[hr, persons, %], recursive=false, params={key=value}}");

        assertEquals(
                new ObjURI("column/hr.persons.%/drop").toString(),
                "ObjURI{type='column', action='drop', objects=[hr, persons, %], recursive=false, params={}}");

        assertEquals(
                new ObjURI("column/hr.persons.%/").toString(),
                "ObjURI{type='column', action='create', objects=[hr, persons, %], recursive=true, params={}}");

        assertEquals(
                new ObjURI("fk/hr.employees/").toString(),
                "ObjURI{type='fk', action='create', objects=[hr, employees], recursive=true, params={}}");*/

    }

}