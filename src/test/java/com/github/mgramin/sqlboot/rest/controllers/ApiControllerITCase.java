/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2017 Maksim Gramin
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiControllerITCase {

    @Autowired
    private TestRestTemplate client;

    @Test
    public void getTextDdl() throws Exception {
        ResponseEntity<String> result = client.getForEntity("/api/table/bookings.airports", String.class);
        System.out.println(result);
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void getTextDdlWithParams() throws Exception {
        ResponseEntity<String> result = client.getForEntity("/api/table/bookings.airports?select=remarks", String.class);
        System.out.println(result);
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void getResourcesBody() throws Exception {
        ResponseEntity<String> result = client.getForEntity("/api/body/table/bookings.airports", String.class);
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void getResourcesHeaders() throws Exception {
        ResponseEntity<String> result = client.getForEntity("/api/headers/table/bookings.airports", String.class);
        assertEquals(200, result.getStatusCodeValue());
    }

}

