package com.github.mgramin.sqlglue.model;

import com.github.mgramin.sqlglue.actions.generator.IActionGenerator;
import com.github.mgramin.sqlglue.util.template_engine.ITemplateEngine;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.*;

/**
 * Created by maksim on 19.05.16.
 */
public class DBSchemaObjectType implements IDBSchemaObjectType {

    protected final static Logger logger = Logger.getLogger(DBSchemaObjectType.class);

    protected DataSource dataSource;
    protected ITemplateEngine templateEngine;

    protected String name;
    protected String sql;
    protected List<DBSchemaObjectType> child;
    protected List<IActionGenerator> commands;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<DBSchemaObjectType> getChild() {
        return child;
    }

    public void setChild(List<DBSchemaObjectType> child) {
        this.child = child;
    }

    public List<IActionGenerator> getCommands() {
        return commands;
    }

    public void setCommands(List<IActionGenerator> commands) {
        this.commands = commands;
    }


    @Override
    public List<DBSchemaObject> scan(List<String> list, Boolean recursive, String action) {
        List<DBSchemaObject> objects = new ArrayList<>();
        try {
            Map<String, Object> data = new HashMap();
            int i=0;
            for (String s : templateEngine.referenceSet(sql)) {
                try {
                    data.put(s, list.get(i++));
                } catch (Throwable t) {
                    data.put(s, '%');
                }
            }

            String prepareSQL = templateEngine.process(data, sql);
            logger.debug(prepareSQL);
            try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(prepareSQL);
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();

                while (resultSet.next()) {
                    DBSchemaObject object = new DBSchemaObject();
                    object.setType(this);

                    Map<String, Object> dataNew = new HashMap();

                    for (int ii = 1; ii < columnCount + 1; ii++) {
                        object.getProperties().setProperty(resultSetMetaData.getColumnName(ii), resultSet.getString(resultSetMetaData.getColumnName(ii)));
                        dataNew.put(resultSetMetaData.getColumnName(ii), resultSet.getString(resultSetMetaData.getColumnName(ii)));
                    }

                    if (commands != null) {
                        for (IActionGenerator command : commands) {
                            if (command.getAction() != null && command.getAction().getAliases().contains(action)) {
                                String sql = command.generate(dataNew);
                                logger.debug(sql);
                                object.setDdl(sql);
                            }
                            /*if (command.getFilePath() != null) {
                                File file = new File("repo/" + templateEngine.process(dataNew, command.getFilePath()));
                                FileUtils.writeStringToFile(file, sql);
                            }*/
                        }
                    }
                    objects.add(object);
                }
            }

            if (recursive && this.child != null) {
                for (DBSchemaObjectType dbSchemaObject : this.child) {

                    objects.addAll(dbSchemaObject.scan(list, true, action));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return objects;
    }


    @Override
    public String toString() {
        return "DBSchemaObjectType{" +
                "dataSource=" + dataSource +
                ", templateEngine=" + templateEngine +
                ", name='" + name + '\'' +
                ", sql='" + sql + '\'' +
                ", child=" + child +
                ", commands=" + commands +
                '}';
    }

}