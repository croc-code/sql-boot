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

package com.github.mgramin.sqlboot.util.template_engine.impl;

import java.util.Arrays;
import java.util.Map;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.tools.template_engine.TemplateEngine;
import com.github.mgramin.sqlboot.tools.template_engine.impl.GroovyTemplateEngine;
import org.junit.Ignore;
import org.junit.Test;
import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.assertEquals;

/**
 * Created by MGramin on 10.12.2016.
 */
public class GroovyTemplateEngineTest {

    @Test
    public void process() throws BootException {
        String txt = "... where lower(c.table_schema) like '$schema'\n" +
            "and lower(c.table_name) like '$table'\n" +
            "and lower(c.column_name) like '$column'";

        String result = "... where lower(c.table_schema) like 'public'\n" +
            "and lower(c.table_name) like 'persons'\n" +
            "and lower(c.column_name) like 'id'";

        Map<String, Object> maps = of("column", "id", "table", "persons", "schema", "public");
        TemplateEngine templateEngine = new GroovyTemplateEngine(txt);
        assertEquals(templateEngine.process(maps), result);
    }

    @Test
    public void processLoweCase() throws BootException {
        Map<String, Object> maps = of("column", "id", "table", "persons", "schema", "public");
        TemplateEngine templateEngine = new GroovyTemplateEngine("create table !{table.toLowerCase()} ...");
        assertEquals(templateEngine.process(maps), "create table persons ...");
    }

    @Test
    @Ignore
    public void getAllProperties() throws BootException {
        String txt = "... where lower(c.table_schema) like '$schema'\n" +
            "and lower(c.table_name) like '$table'\n" +
            "and lower(c.column_name) like '$column'";
        TemplateEngine templateEngine = new GroovyTemplateEngine(txt);
        assertEquals(templateEngine.getAllProperties(),
            Arrays.asList("schema", "table", "column"));
    }

}