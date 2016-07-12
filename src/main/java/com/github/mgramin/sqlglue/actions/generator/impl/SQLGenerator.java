package com.github.mgramin.sqlglue.actions.generator.impl;

import com.github.mgramin.sqlglue.actions.generator.AbstractActionGenerator;
import com.github.mgramin.sqlglue.util.template_engine.ITemplateEngine;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Created by maksim on 05.04.16.
 */
public class SQLGenerator extends AbstractActionGenerator {

    protected final static Logger logger = Logger.getLogger(SQLGenerator.class);

    private DataSource dataSource;
    private ITemplateEngine templateEngine;

    private String prepareSql;
    private String sql;

    @Override
    public String generate(Map<String, Object> variables) {
        String result = null;
        try {
            Connection connection = dataSource.getConnection();

            if (prepareSql != null) {
                Statement prepareStatement = connection.createStatement();
                prepareStatement.execute(prepareSql);
            }
            Statement statement = connection.createStatement();
            logger.trace(templateEngine.process(variables, sql));
            ResultSet resultSet = statement.executeQuery(templateEngine.process(variables, sql));
            resultSet.next();
            result = resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
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

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }


    @Override
    public String toString() {
        return "SQLGenerator{" +
                "sql='" + sql + '\'' +
                '}';
    }

}