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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 200de65b2d417fd603a805be48192d2af3b3a66a $
 * @since 0.1
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [Application::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiControllerITCase {

    @Autowired
    private val client: TestRestTemplate? = null


    @ParameterizedTest
    @CsvSource(
            "204#/api/h2/table/foo",
            "200#/api/h2/table/BOOKINGS",
            "200#/api/h2/table",
            "200#/api/h2/table/BOOKINGS.AIRCRAFTS",
            "204#/api/h2/table/not_exist_schema",
            "200#/api/h2/table",
            "204#/api/h2/not_exist_type",
            "200#/api/h2/table/BOOKINGS.AIRPORTS?select=remarks",
            delimiter = '#')
    fun testHeaders(code: Int, uri: String) {
        val headers = HttpHeaders()
        headers.contentType = MediaType.TEXT_PLAIN
        val result = client!!.exchange(uri, HttpMethod.GET, HttpEntity<Any>(headers), String::class.java)
        assertEquals(code, result.statusCodeValue)
    }


    @ParameterizedTest
    @CsvSource(
            "200#6#/api/meta/h2/table",
            "200#6#/api/meta/h2/table/foo",
            "200#6#/api/meta/h2/table/foo.bar",
            "200#8#/api/meta/h2/column",
            "200#8#/api/meta/h2/column/foo",
            "200#8#/api/meta/h2/column/foo.bar",
            delimiter = '#'
    )
    fun testHeadersMeta(code: Int, fieldCount: Int, uri: String) {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val result = client!!.exchange(uri, HttpMethod.GET, HttpEntity<Any>(headers), List::class.java)
        assertEquals(code, result.statusCodeValue)
        assertEquals(fieldCount, result.body.count())
    }

}
