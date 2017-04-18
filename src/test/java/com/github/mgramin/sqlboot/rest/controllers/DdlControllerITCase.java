package com.github.mgramin.sqlboot.rest.controllers;

import com.github.mgramin.sqlboot.rest.RestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
@ActiveProfiles("information_schema")
public class DdlControllerITCase {

    @Autowired
    private TestRestTemplate client;

    @Test
    public void testFilter() throws Exception {
        callRestAndValidate("t/hr?table_type=system_table");
    }

    @Test
    public void getTextDdl() throws Exception {
        callRestAndValidate("t");
        callRestAndValidate("t/");
        callRestAndValidate("fk");
        callRestAndValidate("fk/");

        callRestAndValidate("t/hr");
        callRestAndValidate("t/hr/+");
        callRestAndValidate("t/hr/create");
        callRestAndValidate("tbl/hr");
        callRestAndValidate("tbl/hr/+");
        callRestAndValidate("tbl/hr/create");
        callRestAndValidate("table/hr");
        callRestAndValidate("table/hr/+");
        callRestAndValidate("table/hr/create");

        callRestAndValidate("t/hr/drop");
        callRestAndValidate("t/hr/-");
        callRestAndValidate("tbl/hr/drop");
        callRestAndValidate("tbl/hr/-");
        callRestAndValidate("table/hr/drop");
        callRestAndValidate("table/hr/-");

        callRestAndValidate("table/hr.jobs/drop");

        /*callRestAndValidate("/api/table/hr/");
        callRestAndValidate("/api/table/hr?type=sql");
        callRestAndValidate("/api/table/hr?type=migration");
        callRestAndValidate("/api/table/hr/drop?type=migration");
        callRestAndValidate("/api/table/hr?type=html");

        callRestAndValidate("/api/fk/hr");
        callRestAndValidate("/api/fk/hr/+");
        callRestAndValidate("/api/fk/hr/-");
        callRestAndValidate("/api/fk/hr?type=sql");

        callRestAndValidate("/api/view/hr");
        callRestAndValidate("/api/mview/hr");*/
    }

    private void callRestAndValidate(String uri) {
        ResponseEntity<String> forEntity = client.getForEntity("/api/" + uri, String.class);
        assertEquals(forEntity.getStatusCodeValue(), 200);
        System.out.println(forEntity.getBody());
    }

}