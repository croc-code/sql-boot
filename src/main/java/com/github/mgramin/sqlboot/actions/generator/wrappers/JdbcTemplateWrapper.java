package com.github.mgramin.sqlboot.actions.generator.wrappers;

import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by maksim on 29.04.17.
 */
public class JdbcTemplateWrapper implements IActionGenerator {


    public JdbcTemplateWrapper(IActionGenerator baseGenerator, DataSource dataSource) {
        this.baseGenerator = baseGenerator;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        List<Map<String, Object>> maps = namedParameterJdbcTemplate.queryForList(baseGenerator.generate(variables), variables);
        return maps.get(1).get("result").toString();
    }

    @Override
    public DBSchemaObjectCommand command() {
        return baseGenerator.command();
    }

    private final IActionGenerator baseGenerator;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

}
