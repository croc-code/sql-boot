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
public class DdlControllerTest {

    @Autowired
    private TestRestTemplate client;

    @Test
    public void getTextDdl2() throws Exception {
        callRestAndValidate("/ddl/table/hr/drop?type=liquibase");
    }

    @Test
    public void getTextDdl() throws Exception {
        callRestAndValidate("/ddl/table/hr");
        callRestAndValidate("/ddl/table/hr/+");
        callRestAndValidate("/ddl/table/hr/create");

        callRestAndValidate("/ddl/table/hr/drop");
        callRestAndValidate("/ddl/table/hr/-");

        callRestAndValidate("/ddl/table/hr/");
        callRestAndValidate("/ddl/table/hr?type=sql");
        callRestAndValidate("/ddl/table/hr?type=liquibase");
        callRestAndValidate("/ddl/table/hr?type=html");
        callRestAndValidate("/ddl/view/hr");
        callRestAndValidate("/ddl/mview/hr");
    }

    private void callRestAndValidate(String uri) {
        ResponseEntity<String> forEntity = client.getForEntity(uri, String.class);
        assertEquals(forEntity.getStatusCodeValue(), 200);
        System.out.println(forEntity.getBody());
    }

}