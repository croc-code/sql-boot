package com.github.mgramin.sqlglue.model;

import com.github.mgramin.sqlglue.actions.generator.IActionGenerator;
import com.github.mgramin.sqlglue.uri.ObjURI;
import com.github.mgramin.sqlglue.util.sql.JdbcSqlHelper;
import com.github.mgramin.sqlglue.util.template_engine.ITemplateEngine;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by maksim on 19.05.16.
 */
public class DBSchemaObjectType implements IDBSchemaObjectType {

    private final static Logger logger = Logger.getLogger(DBSchemaObjectType.class);

    private JdbcSqlHelper sqlHelper;
    private ITemplateEngine templateEngine;


    private String name;
    private String sql;
    private List<DBSchemaObjectType> child;
    private List<IActionGenerator> commands;

    @Override
    public Map<String, DBSchemaObject> scan(List<String> list, String action, Boolean recursive) {
        Map<String, DBSchemaObject> objects = new LinkedHashMap<>();
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

            for (Map<String, String> stringStringMap : sqlHelper.select(prepareSQL)) {
                DBSchemaObject object = new DBSchemaObject();
                object.setPaths(stringStringMap);
                Map<String, Object> dataNew = new LinkedHashMap<>();
                for (Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {
                    if (!stringStringEntry.getKey().startsWith("@")) {
                        dataNew.put(stringStringEntry.getKey(), stringStringEntry.getValue());
                        object.setName(stringStringEntry.getValue());
                    } else {
                        if (stringStringEntry.getValue() != null) {
                            object.addProperty(stringStringEntry.getKey().substring(1), stringStringEntry.getValue());
                        }
                        else {
                            object.addProperty(stringStringEntry.getKey().substring(1), "");
                        }

                    }
                }
                ObjURI objURI = new ObjURI();
                List<Object> l = new ArrayList<>(dataNew.values());
                List<String> strings = (List<String>) (Object) l;

                objURI.setType(this.getName());
                objURI.setObjects(strings);
                object.setObjURI(objURI);

                object.setType(this);
                objects.put(object.getObjURI().toString(), object);

                for (IActionGenerator generator : ((DBSchemaObjectType) object.getType()).getCommands()) {
                    if (generator.getFilePath() != null) {
                        System.out.println("repo/" + templateEngine.process(dataNew, generator.getFilePath()));
                    }
                }
            }

            if (this.child != null) {
                for (DBSchemaObjectType dbSchemaObject : this.child) {
                    objects.putAll(dbSchemaObject.scan(list, action, true));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }


    public static Logger getLogger() {
        return logger;
    }

    public JdbcSqlHelper getSqlHelper() {
        return sqlHelper;
    }

    public void setSqlHelper(JdbcSqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
    }

    public ITemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
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
    public String toString() {
        return "DBSchemaObjectType{" +
                "name='" + name + '\'' +
                '}';
    }

}