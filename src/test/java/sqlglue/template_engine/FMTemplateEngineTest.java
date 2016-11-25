package sqlglue.template_engine;

import com.github.mgramin.sqlglue.util.template_engine.ITemplateEngine;
import com.github.mgramin.sqlglue.util.template_engine.impl.FMTemplateEngine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by maksim on 02.04.16.
 */
public class FMTemplateEngineTest {

    @org.junit.Test
    public void testProcess() throws Exception {
        ITemplateEngine t = new FMTemplateEngine();

        String txt = "... where lower(c.table_schema) like '${@schema}'\n" +
                           "and lower(c.table_name) like '${@table}'\n" +
                           "and lower(c.column_name) like '${@column}'";

        String result = "... where lower(c.table_schema) like 'public'\n" +
                              "and lower(c.table_name) like 'persons'\n" +
                              "and lower(c.column_name) like 'id'";

        Map<String, Object> maps = new HashMap<>();
        maps.put("@column", "id");
        maps.put("@schema", "public");
        maps.put("@table", "persons");

        assertEquals(t.process(maps, txt), result);
    }

    @org.junit.Test
    public void testGetAllProperties() throws Exception {
        ITemplateEngine t = new FMTemplateEngine();
        String txt = "... where lower(c.table_schema) like '${@schema}'\n" +
                           "and lower(c.table_name) like '${@table}'\n" +
                           "and lower(c.column_name) like '${@column}'";
        assertEquals(t.getAllProperties(txt), Arrays.asList("@schema", "@table", "@column"));
    }

}