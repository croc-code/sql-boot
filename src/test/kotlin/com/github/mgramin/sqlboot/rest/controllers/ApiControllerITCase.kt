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

import com.github.mgramin.sqlboot.rest.Application
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 200de65b2d417fd603a805be48192d2af3b3a66a $
 * @since 0.1
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(Application::class), webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiControllerITCase {

    @Autowired
    private val client: TestRestTemplate? = null

    @Test
    fun getText() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.TEXT_PLAIN
        val result = client!!.exchange(
                "/api/h2/table/BOOKINGS.AIRCRAFTS",
                HttpMethod.GET,
                HttpEntity<Any>(headers),
                String::class.java)
        assertEquals(200, result.statusCodeValue.toLong())
    }

    @Test
    fun getEmptyText() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.TEXT_PLAIN
        val result = client!!.exchange("/api/h2/table/not_exist_schema", HttpMethod.GET, HttpEntity<Any>(headers), String::class.java)
        assertEquals(204, result.statusCodeValue.toLong())
    }

    @Test
    fun getTextDdl2() {
        val result = client!!.getForEntity("/api/h2/table", String::class.java)
        println(result)
        assertEquals(200, result.statusCodeValue.toLong())
    }

    @Test
    fun getTextDdlWithParams() {
        val result = client!!.getForEntity("/api/h2/table/BOOKINGS.AIRPORTS?select=remarks", String::class.java)
        assertEquals(200, result.statusCodeValue.toLong())
    }

    @Test
    fun getResourcesHeaders() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.TEXT_PLAIN
        val result = client!!.exchange("/api/h2/headers/table/BOOKINGS", HttpMethod.GET, HttpEntity<Any>(headers), String::class.java)
        assertEquals(200, result.statusCodeValue.toLong())
    }

    @Test
    fun getResourcesEmptyHeaders() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.TEXT_PLAIN
        val result = client!!.exchange("/api/h2/headers/table/foo", HttpMethod.GET, HttpEntity<Any>(headers), String::class.java)
        assertEquals(204, result.statusCodeValue.toLong())
    }

    @Test
    fun getResourcesHeaders2() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.TEXT_PLAIN
        val result = client!!.exchange("/api/h2/headers/table", HttpMethod.GET, HttpEntity<Any>(headers), String::class.java)
        assertEquals(200, result.statusCodeValue.toLong())
    }
}
