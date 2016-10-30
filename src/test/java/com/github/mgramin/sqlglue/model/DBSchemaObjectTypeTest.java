package com.github.mgramin.sqlglue.model;

import com.github.mgramin.sqlglue.util.sql.ISqlHelper;
import com.github.mgramin.sqlglue.util.template_engine.ITemplateEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mgramin on 27.10.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class DBSchemaObjectTypeTest {

    @Test
    public void scan() throws Exception {
        ISqlHelper sqlHelper = mock(ISqlHelper.class);

        List list = new ArrayList<Map<String, String>>();
        Map map = new HashMap<String, String>();
        map.put("schema", "public");
        map.put("table", "persons");
        map.put("column", "id");
        list.add(map);
        when(sqlHelper.select(any())).thenReturn(list);

        ITemplateEngine templateEngine = mock(ITemplateEngine.class);
        when(templateEngine.referenceSet(any())).thenReturn(Arrays.asList("@schema", "@table", "@column"));



        DBSchemaObjectType type = new DBSchemaObjectType();
        type.setSqlHelper(sqlHelper);
        type.setTemplateEngine(templateEngine);
        type.setSql("this is test sql query");

        type.scan(Arrays.asList("HR", "persons"), "create", false);
    }

}