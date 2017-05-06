package com.github.mgramin.sqlboot.rest.controllers;

import com.github.mgramin.sqlboot.rest.RestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
@ActiveProfiles("information_schema")
public class SqlExecuteControllerITCase {

    @Autowired
    private TestRestTemplate restClient;

    @Test
    public void execSql2Xml() throws Exception {
        ResponseEntity<String> forEntity = this.restClient.exchange(
                "/exec?sql=select 1 as one, 2 as two", HttpMethod.GET, null, String.class);

        assertEquals(forEntity.getBody(), "[{\"ONE\":\"1\",\"TWO\":\"2\"}]");
        assertEquals(forEntity.getHeaders().getContentType(), MediaType.APPLICATION_JSON_UTF8);
    }

    @Test
    public void execSql2XmlPost() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
        HttpEntity<String> entity = new HttpEntity<>("select 1 as one, 2 as two", headers);

        ResponseEntity<String> forEntity = restClient.exchange("/exec", HttpMethod.POST, entity, String.class);

        assertEquals(forEntity.getBody(), "<List><item><ONE>1</ONE><TWO>2</TWO></item></List>");
        //assertEquals(forEntity.headers().getContentType(), MediaType.APPLICATION_XML_VALUE);
    }

    @Test
    public void execSql2Json() throws Exception {
        ResponseEntity<String> forEntity = this.restClient.exchange(
                "/exec?sql=select 1 as one, 2 as two", HttpMethod.GET, null, String.class);

        assertEquals(forEntity.getBody(), "[{\"ONE\":\"1\",\"TWO\":\"2\"}]");
        assertEquals(forEntity.getHeaders().getContentType(), MediaType.APPLICATION_JSON_UTF8);
    }

    @Test
    public void execSql2JsonPost() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("select 1 as one, 2 as two", headers);

        ResponseEntity<String> forEntity = restClient.exchange("/exec", HttpMethod.POST, entity, String.class);
        System.out.println(forEntity.getBody());

        assertEquals(forEntity.getBody(), "[{\"ONE\":\"1\",\"TWO\":\"2\"}]");
        assertEquals(forEntity.getHeaders().getContentType(), MediaType.APPLICATION_JSON_UTF8);
    }

}