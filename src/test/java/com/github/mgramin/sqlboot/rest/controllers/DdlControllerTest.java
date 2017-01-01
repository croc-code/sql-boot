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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
@ActiveProfiles("information_schema")
public class DdlControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void getTextDdl() throws Exception {
        ResponseEntity<String> forEntity = this.testRestTemplate.getForEntity(
                "/ddl/table/hr.jobs", String.class);

        System.out.println(forEntity.getStatusCodeValue());
        System.out.println(forEntity.toString());
    }

}