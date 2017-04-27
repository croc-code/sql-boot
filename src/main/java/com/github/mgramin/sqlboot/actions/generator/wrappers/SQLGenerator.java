package com.github.mgramin.sqlboot.actions.generator.wrappers;

import com.github.mgramin.sqlboot.actions.generator.AbstractActionGenerator;
import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObjectCommand;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;
import com.github.mgramin.sqlboot.util.template_engine.ITemplateEngine;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by maksim on 05.04.16.
 */
@ToString
@Deprecated
public class SQLGenerator extends AbstractActionGenerator implements IActionGenerator {

    public SQLGenerator(List<String> sql, DBSchemaObjectCommand command, ITemplateEngine templateEngine, ISqlHelper sqlHelper) {
        this.sql = sql;
        this.templateEngine = templateEngine;
        this.sqlHelper = sqlHelper;
        this.dbSchemaObjectCommand = command;
    }

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        List<String> readySQL = new ArrayList<>();
        for (String s : sql) {
            readySQL.add(templateEngine.process(variables));
        }
        List<Map<String, String>> maps = sqlHelper.selectBatch(readySQL);
        return maps.get(0).entrySet().iterator().next().getValue();
    }

    private String prepareSql;
    private List<String> sql;
    private ISqlHelper sqlHelper;
    @Deprecated private ITemplateEngine templateEngine;

}