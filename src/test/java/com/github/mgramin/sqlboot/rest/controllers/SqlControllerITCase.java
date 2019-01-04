/*
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2019 Maksim Gramin
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.rest.controllers;

import static org.junit.Assert.assertEquals;

import com.github.mgramin.sqlboot.rest.Application;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class SqlControllerITCase {

    @Autowired
    private TestRestTemplate restClient;

    @Test
    @Ignore
    public void execSql2Xml() throws Exception {
        final ResponseEntity<String> forEntity = this.restClient.exchange(
            "/sql", HttpMethod.GET, new HttpEntity<>("select 1 as one, 2 as two"), String.class);
        assertEquals(forEntity.getBody(), "[{\"ONE\":\"1\",\"TWO\":\"2\"}]");
        assertEquals(forEntity.getHeaders().getContentType(), MediaType.APPLICATION_JSON_UTF8);
    }

    @Test
    @Ignore
    public void execSql2Json() throws Exception {
        final ResponseEntity<String> forEntity = this.restClient.exchange(
            "/sql", HttpMethod.GET, new HttpEntity<>("select 1 as one, 2 as two"), String.class);

        assertEquals(forEntity.getBody(), "[{\"ONE\":\"1\",\"TWO\":\"2\"}]");
        assertEquals(forEntity.getHeaders().getContentType(), MediaType.APPLICATION_JSON_UTF8);
    }

    @Test
    @Ignore
    public void execSql2JsonPost() throws Exception {
        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("select 1 as one, 2 as two", headers);

        ResponseEntity<String> forEntity = restClient
            .exchange("/sql", HttpMethod.POST, entity, String.class);
        System.out.println(forEntity.getBody());

        assertEquals(forEntity.getBody(), "[{\"ONE\":\"1\",\"TWO\":\"2\"}]");
        assertEquals(forEntity.getHeaders().getContentType(), MediaType.APPLICATION_JSON_UTF8);
    }

}