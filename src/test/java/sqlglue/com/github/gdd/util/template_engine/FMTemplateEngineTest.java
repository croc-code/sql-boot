package sqlglue.com.github.gdd.util.template_engine;

import com.github.mgramin.sqlglue.util.template_engine.impl.FMTemplateEngine;
import com.github.mgramin.sqlglue.util.template_engine.ITemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by maksim on 02.04.16.
 */
public class FMTemplateEngineTest {

    @org.junit.Test
    public void testProcess() throws Exception {
        ITemplateEngine templateEngine = new FMTemplateEngine();
        Map<String, Object> maps = new HashMap<>();
        maps.put("name", "World");
        String result = templateEngine.process(maps, "Hello, ${name}!");
        assertEquals(result, "Hello, World!");
    }

    @org.junit.Test
    public void testProcess2() throws Exception {
        ITemplateEngine templateEngine = new FMTemplateEngine();
        Map<String, Object> maps = new HashMap<>();
        maps.put("name", "World");
        String result = templateEngine.process(maps, "Hello, World!");
        assertEquals(result, "Hello, World!");
    }


}