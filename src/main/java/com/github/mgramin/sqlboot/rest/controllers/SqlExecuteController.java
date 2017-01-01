package com.github.mgramin.sqlboot.rest.controllers;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by mgramin on 31.12.2016.
 */
@RestController
@ImportResource("classpath:config.xml")
public class SqlExecuteController {

    @Autowired
    ISqlHelper sqlHelper;

    @RequestMapping(value = "/exec", produces = {MediaType.APPLICATION_XML_VALUE})
    List<Map<String, String>> execSqlXml(
            @RequestParam("sql") String sql,
            HttpServletRequest request, HttpServletResponse response) throws SqlBootException {
        return sqlHelper.select(sql);
    }

    @RequestMapping(value = "/exec", method = RequestMethod.POST, produces = {MediaType.APPLICATION_XML_VALUE})
    List<Map<String, String>> execSqlXmlPost(
            @RequestBody() String sql,
            HttpServletRequest request, HttpServletResponse response) throws SqlBootException {
        return sqlHelper.select(sql);
    }

    @RequestMapping(value = "/exec", produces = {MediaType.APPLICATION_JSON_VALUE})
    List<Map<String, String>> execSqlJson(
            @RequestParam("sql") String sql,
            HttpServletRequest request, HttpServletResponse response) throws SqlBootException {
        return sqlHelper.select(sql);
    }

}
