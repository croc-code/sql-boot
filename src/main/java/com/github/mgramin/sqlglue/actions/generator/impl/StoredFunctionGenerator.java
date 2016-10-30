package com.github.mgramin.sqlglue.actions.generator.impl;

import com.github.mgramin.sqlglue.actions.generator.AbstractActionGenerator;
import com.github.mgramin.sqlglue.actions.generator.IActionGenerator;
import com.github.mgramin.sqlglue.util.template_engine.ITemplateEngine;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mgramin on 24.10.2016.
 */
public class StoredFunctionGenerator extends AbstractActionGenerator {

    public DataSource dataSource;
    public ITemplateEngine templateEngine;
    public String prepareSql;
    public String functionName;

    @Override
    public String generate(Map<String, Object> variables) {
        try (Connection connection = dataSource.getConnection()) {
            CallableStatement statement = connection.prepareCall("{? = call getVersion()}");
            statement.registerOutParameter(1, Types.VARCHAR);
            statement.execute();
            return statement.getString(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ITemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String getPrepareSql() {
        return prepareSql;
    }

    public void setPrepareSql(String prepareSql) {
        this.prepareSql = prepareSql;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
}
