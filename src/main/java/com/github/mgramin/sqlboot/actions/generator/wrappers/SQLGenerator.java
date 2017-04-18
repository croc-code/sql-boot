package com.github.mgramin.sqlboot.actions.generator.wrappers;

import com.github.mgramin.sqlboot.actions.generator.AbstractActionGenerator;
import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;
import com.github.mgramin.sqlboot.util.template_engine.ITemplateEngine;

import java.util.*;

/**
 * Created by maksim on 05.04.16.
 */
@Deprecated
public class SQLGenerator extends AbstractActionGenerator implements IActionGenerator {

    public String prepareSql;
    public List<String> sql;
    private ISqlHelper sqlHelper;

    @Deprecated
    public ITemplateEngine templateEngine;

    public void setSql(List<String> sql) {
        this.sql = sql;
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

    public SQLGenerator() {
    }

    public SQLGenerator(ISqlHelper sqlHelper, ITemplateEngine templateEngine, List<String> sql) {
        this.sqlHelper = sqlHelper;
        this.templateEngine = templateEngine;
        this.sql = sql;
    }


    public void setTemplateEngine(ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void setPrepareSql(String prepareSql) {
        this.prepareSql = prepareSql;
    }

    public ISqlHelper getSqlHelper() {
        return sqlHelper;
    }

    public void setSqlHelper(ISqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
    }

    @Override
    public String toString() {
        return "SQLGenerator{" +
                "db.sql='" + sql + '\'' +
                '}';
    }

}