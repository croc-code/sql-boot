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

package com.github.mgramin.sqlboot.uri;

import java.net.URISyntaxException;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.uri.Uri;
import com.github.mgramin.sqlboot.model.uri.impl.DbUri;
import com.github.mgramin.sqlboot.model.uri.wrappers.JsonWrapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by maksim on 12.06.16.
 */
public class DbUriTest {

    @Test
    public void createAllTableFromAllSchema() {
        test("table/*", "DbUri{type='table', dbSchemaObjectCommand='null', path=[*], recursive=false, params={}}");
    }

    @Test
    public void createAllTableFromSchema() {
        test("table/hr", "DbUri{type='table', dbSchemaObjectCommand='null', path=[hr], recursive=false, params={}}");
        test("table/hr.*", "DbUri{type='table', dbSchemaObjectCommand='null', path=[hr, *], recursive=false, params={}}");
    }

    @Test
    public void createAllTableWithChildObjectsFromSchema() throws BootException {
        test("table/hr.*/", "DbUri{type='table', dbSchemaObjectCommand='null', path=[hr, *], recursive=true, params={}}");
    }

    @Test
    public void dropAllTableFromSchema() throws BootException {
        test("table/hr.*/drop", "DbUri{type='table', dbSchemaObjectCommand='drop', path=[hr, *], recursive=false, params={}}");
    }


    @Test
    public void createColumnsForTable() throws BootException {
        test("column/hr.persons.*name",
                "DbUri{type='column', dbSchemaObjectCommand='null', path=[hr, persons, *name], recursive=false, params={}}");
    }

    @Test
    public void dropColumnFromTable() throws BootException {
        test("column/hr.persons.name/drop",
                "DbUri{type='column', dbSchemaObjectCommand='drop', path=[hr, persons, name], recursive=false, params={}}");
    }


    @Test
    public void createAllFkForTable() throws BootException {
        test("fk/hr.employees.*",
                "DbUri{type='fk', dbSchemaObjectCommand='null', path=[hr, employees, *], recursive=false, params={}}");
    }

    @Test
    public void dropAllFkFromTable() throws BootException {
        test("fk/hr.employees.*/drop",
                "DbUri{type='fk', dbSchemaObjectCommand='drop', path=[hr, employees, *], recursive=false, params={}}");
    }

    @Test
    public void disableAllFkFromTable() throws BootException {
        test("fk/hr.employees.*/disable",
                "DbUri{type='fk', dbSchemaObjectCommand='disable', path=[hr, employees, *], recursive=false, params={}}");
    }

    @Test
    public void disableAllFkFromSchema() throws BootException {
        test("fk/hr.*.*/disable",
                "DbUri{type='fk', dbSchemaObjectCommand='disable', path=[hr, *, *], recursive=false, params={}}");
    }

    @Test
    public void testDefaultActionIsCreate() throws BootException {
        test("fk/hr.*.*",
                "DbUri{type='fk', dbSchemaObjectCommand='null', path=[hr, *, *], recursive=false, params={}}");
    }


    @Test
    public void testParams() throws BootException, URISyntaxException {
        test("t/hr?@table_comment=big_table",
            "DbUri{type='t', dbSchemaObjectCommand='null', path=[hr], recursive=false, params={@table_comment=big_table}}");
        test("table/hr?@table_comment=big_table",
            "DbUri{type='table', dbSchemaObjectCommand='null', path=[hr], recursive=false, params={@table_comment=big_table}}");
    }


    private void test(String uriString, String jsonExpected) throws BootException {
        Uri uri = new DbUri(uriString);
        assertEquals(uriString, uri.toString());
        assertEquals(new JsonWrapper(new DbUri(uriString)).toString(), jsonExpected);
    }

}