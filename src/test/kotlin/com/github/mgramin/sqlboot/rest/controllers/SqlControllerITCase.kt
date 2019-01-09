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

package com.github.mgramin.sqlboot.rest.controllers

import org.junit.Assert.assertEquals

import com.github.mgramin.sqlboot.rest.Application
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(Application::class), webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = arrayOf("management.port=0"))
class SqlControllerITCase {

    @Autowired
    private val restClient: TestRestTemplate? = null

    @Test
    @Ignore
    @Throws(Exception::class)
    fun execSql2Xml() {
        val forEntity = this.restClient!!.exchange(
                "/sql", HttpMethod.GET, HttpEntity("select 1 as one, 2 as two"), String::class.java)
        assertEquals(forEntity.body, "[{\"ONE\":\"1\",\"TWO\":\"2\"}]")
        assertEquals(forEntity.headers.contentType, MediaType.APPLICATION_JSON_UTF8)
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun execSql2Json() {
        val forEntity = this.restClient!!.exchange(
                "/sql", HttpMethod.GET, HttpEntity("select 1 as one, 2 as two"), String::class.java)

        assertEquals(forEntity.body, "[{\"ONE\":\"1\",\"TWO\":\"2\"}]")
        assertEquals(forEntity.headers.contentType, MediaType.APPLICATION_JSON_UTF8)
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun execSql2JsonPost() {
        val headers = HttpHeaders()
        //        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        val entity = HttpEntity("select 1 as one, 2 as two", headers)

        val forEntity = restClient!!
                .exchange("/sql", HttpMethod.POST, entity, String::class.java)
        println(forEntity.body)

        assertEquals(forEntity.body, "[{\"ONE\":\"1\",\"TWO\":\"2\"}]")
        assertEquals(forEntity.headers.contentType, MediaType.APPLICATION_JSON_UTF8)
    }

}