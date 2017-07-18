/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Maksim Gramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.rest.controllers.h2;

import com.github.mgramin.sqlboot.rest.RestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
public class ApiControllerITCase {

    @Autowired
    private TestRestTemplate client;

    @Test
    public void testFilter() {
        callRestAndValidate("t/hr?table_type=system_table");
    }

    @Test
    public void getOneTable() {
         callRestAndValidate("table/hr.jobs");
    }

    @Test
    public void getOneTableWithChildObjects() {
         callRestAndValidate("table/hr.jobs/");
    }

    @Test
    public void getOneTableHtml() {
         callRestAndValidate("table/hr.jobs?type=html");
    }

    @Test
    public void getOneTableMigration() {
         callRestAndValidate("table/hr.jobs?type=html");
    }

    @Test
    public void getOneTableJson() {
         callRestAndValidate("table/hr.jobs?type=json");
    }

   @Test
    public void getTextDdl() {
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
    }

    @Test
    public void getFkDdl() {
        callRestAndValidate("fk");
    }

    @Test
    public void getTableDdl() {
        callRestAndValidate("table/hr");
    }

    @Test
    public void getTableJsonData() {
        callRestAndValidate("data/hr.users?type=json");
    }

    @Test
    public void getTableJsonDataWithLimit() {
        callRestAndValidate("data/hr.users?type=json&limit=1");
    }

    @Test
    public void getTableXmlData() {
        callRestAndValidate("data/hr.users?type=xml");
    }

    @Test
    public void getTableSqlInsertData() {
        callRestAndValidate("data/hr.users/create");
    }


    @Test
    public void getTableSqlInZip() {
        callRestAndValidate("table/hr.users/create?type=zip");
    }

    @Test
    public void getTableNotFound() {
        callRestAndValidate("table/foo.bar", 204);
    }

    private void callRestAndValidate(String uri) {
        System.out.println("<<< " + uri);
        ResponseEntity<String> forEntity = client.getForEntity("/api/" + uri, String.class);
        assertEquals(200, forEntity.getStatusCodeValue());
        System.out.println(forEntity.getBody());
    }

    private void callRestAndValidate(String uri, int code) {
        System.out.println("<<< " + uri);
        ResponseEntity<String> forEntity = client.getForEntity("/api/" + uri, String.class);
        assertEquals(code, forEntity.getStatusCodeValue());
        System.out.println(forEntity.getBody());
    }

}