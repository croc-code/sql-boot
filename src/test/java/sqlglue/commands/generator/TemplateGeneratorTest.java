package sqlglue.commands.generator;

import com.github.mgramin.sqlglue.actions.generator.IActionGenerator;
import com.github.mgramin.sqlglue.actions.generator.impl.TemplateGenerator;
import com.github.mgramin.sqlglue.util.template_engine.impl.FMTemplateEngine;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by maksim on 08.04.16.
 */
public class TemplateGeneratorTest {

    @Test
    public void testGenerate() throws Exception {
        Map<String, Object> maps = new HashMap<>();
        maps.put("name", "World");
        IActionGenerator commandGenerator = new TemplateGenerator(new FMTemplateEngine(), "Hello, ${name}!");
        assertEquals(commandGenerator.generate(maps), "Hello, World!");
    }
}