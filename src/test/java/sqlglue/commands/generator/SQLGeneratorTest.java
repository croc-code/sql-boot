package sqlglue.commands.generator;

import com.github.mgramin.sqlglue.actions.generator.IActionGenerator;
import com.github.mgramin.sqlglue.actions.generator.impl.SQLGenerator;
import com.github.mgramin.sqlglue.util.template_engine.ITemplateEngine;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by maksim on 08.04.16.
 */
public class SQLGeneratorTest {

    @Test
    public void testGenerate() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("db_config.xml");

        Map<String, Object> maps = new HashMap<>();
        maps.put("name", "World");

        DataSource dataSource = context.getBean("dataSource", DataSource.class);
        ITemplateEngine templateEngine = context.getBean("templateEngine", ITemplateEngine.class);

        IActionGenerator sqlGenerator = new SQLGenerator(dataSource, templateEngine ,"select 'test query' from dual");


        assertEquals("test query", sqlGenerator.generate(maps));
    }

}