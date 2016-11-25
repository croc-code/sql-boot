package com.github.mgramin.sqlboot.actions.generator.impl;

import com.github.mgramin.sqlboot.actions.generator.AbstractActionGenerator;
import com.github.mgramin.sqlboot.actions.generator.IActionGenerator;
import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.util.template_engine.ITemplateEngine;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Created by maksim on 05.04.16.
 */
public class SQLGenerator extends AbstractActionGenerator implements IActionGenerator {

    public DataSource dataSource;
    public ITemplateEngine templateEngine;
    public String prepareSql;
    public String sql;

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String generate(Map<String, Object> variables) throws SqlBootException {
        String result = null;
        try (Connection connection = dataSource.getConnection()) {
            if (prepareSql != null) {
                Statement prepareStatement = connection.createStatement();
                prepareStatement.execute(prepareSql);
            }
            Statement statement = connection.createStatement();
            String txt = templateEngine.process(variables, sql);
            logger.trace(txt);
            ResultSet resultSet = statement.executeQuery(txt);
            resultSet.next();
            result = resultSet.getString(1);
        } catch (SQLException e) {
            throw new SqlBootException(e);
        }
        return result;
    }

    public SQLGenerator() {
    }

    public SQLGenerator(DataSource dataSource, ITemplateEngine templateEngine, String sql) {
        this.dataSource = dataSource;
        this.templateEngine = templateEngine;
        this.sql = sql;
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setTemplateEngine(ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void setPrepareSql(String prepareSql) {
        this.prepareSql = prepareSql;
    }

    @Override
    public String toString() {
        return "SQLGenerator{" +
                "db.sql='" + sql + '\'' +
                '}';
    }

}