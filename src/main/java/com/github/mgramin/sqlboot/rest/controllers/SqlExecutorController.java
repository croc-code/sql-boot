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

package com.github.mgramin.sqlboot.rest.controllers;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.sql.impl.JdbcSqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mgramin on 31.12.2016.
 */
@RestController
@ComponentScan(basePackages = "com.github.mgramin.sqlboot")
@EnableAutoConfiguration
public final class SqlExecutorController {

    @Autowired
    DataSource dataSource;

    @RequestMapping(value = "exec", produces = {MediaType.APPLICATION_XML_VALUE})
    public List<Map<String, String>> execSql2Xml(@RequestParam("sql") String sql) throws BootException {
        return new JdbcSqlQuery(dataSource).select(sql);
    }

    @RequestMapping(value = "exec", method = RequestMethod.POST, produces = {MediaType.APPLICATION_XML_VALUE})
    public List<Map<String, String>> execSql2XmlPost(@RequestBody() String sql) throws BootException {
        return new JdbcSqlQuery(dataSource).select(sql);
    }

    @RequestMapping(value = "exec", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Map<String, String>> execSql2Json(@RequestParam("sql") String sql) throws BootException {
        return new JdbcSqlQuery(dataSource).select(sql);
    }

    @RequestMapping(value = "exec", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Map<String, String>> execSql2JsonPost(@RequestBody() String sql) throws BootException {
        return new JdbcSqlQuery(dataSource).select(sql);
    }

}
