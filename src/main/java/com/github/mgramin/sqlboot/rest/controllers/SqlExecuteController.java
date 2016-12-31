package com.github.mgramin.sqlboot.rest.controllers;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;
import org.codehaus.jackson.map.annotate.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/sql", method = RequestMethod.GET, produces = {MediaType.TEXT_PLAIN_VALUE})
    public @ResponseBody
    List<Map<String, String>> execSql(HttpServletRequest request, HttpServletResponse response) throws SqlBootException {
        List<Map<String, String>> select = sqlHelper.select(
                "select table_catalog, table_schema, table_name from information_schema.tables");

        return select;
    }

}
