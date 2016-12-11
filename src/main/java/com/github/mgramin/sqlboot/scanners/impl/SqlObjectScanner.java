package com.github.mgramin.sqlboot.scanners.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.model.DBSchemaObjectType;
import com.github.mgramin.sqlboot.scanners.AbstractObjectScanner;
import com.github.mgramin.sqlboot.scanners.IObjectScanner;
import com.github.mgramin.sqlboot.uri.ObjURI;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;
import com.github.mgramin.sqlboot.util.template_engine.ITemplateEngine;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Custom-SQL object scanner
 */
public class SqlObjectScanner extends AbstractObjectScanner implements IObjectScanner {

    private final static Logger logger = Logger.getLogger(DBSchemaObjectType.class);

    public ISqlHelper sqlHelper;
    public ITemplateEngine templateEngine;
    public String sql;
    public String prepareSql;


    public SqlObjectScanner() {
    }

    public SqlObjectScanner(ISqlHelper sqlHelper, ITemplateEngine templateEngine, String sql, String prepareSql) {
        this.sqlHelper = sqlHelper;
        this.templateEngine = templateEngine;
        this.sql = sql;
        this.prepareSql = prepareSql;
    }

    @Override
    public Map<String, DBSchemaObject> scan(ObjURI objURI, DBSchemaObjectType type) throws SqlBootException {
        List<String> list = objURI.getObjects();

        Map<String, DBSchemaObject> objects = new LinkedHashMap<>();
        try {
            Map<String, Object> data = new HashMap();
            int i=0;
            for (String s : templateEngine.getAllProperties(sql)) {
                try {
                    data.put(s, list.get(i++));
                } catch (Throwable t) {
                    data.put(s, '%');
                }
            }

            String prepareSQL = templateEngine.process(data, sql);
            logger.debug(prepareSQL);

            List<Map<String, String>> select = sqlHelper.select(prepareSQL);
            for (Map<String, String> stringStringMap : select) {
                DBSchemaObject object = new DBSchemaObject();
                object.paths = stringStringMap;
                List<String> objectsForUri = new ArrayList<>();
                for (Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {
                    if (!stringStringEntry.getKey().startsWith("@")) {
                        objectsForUri.add(stringStringEntry.getValue());
                        object.name = stringStringEntry.getValue();
                    } else {
                        if (stringStringEntry.getValue() != null) {
                            object.addProperty(stringStringEntry.getKey().substring(1), stringStringEntry.getValue());
                        }
                        else {
                            object.addProperty(stringStringEntry.getKey().substring(1), "");
                        }
                    }
                }
                object.objURI = new ObjURI(type.name, objectsForUri);
                object.type = type;
                objects.put(object.objURI.toString(), object);
            }

        } catch (Exception e) {
            throw new SqlBootException(e);
        }
        return objects;
    }

    public void setSqlHelper(ISqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
    }

    public void setTemplateEngine(ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setPrepareSql(String prepareSql) {
        this.prepareSql = prepareSql;
    }

    @Override
    public String toString() {
        return "SqlObjectScanner{" +
                "sql='" + sql + '\'' +
                '}';
    }

}