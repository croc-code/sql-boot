package com.github.mgramin.sqlglue.scanners.impl;

import com.github.mgramin.sqlglue.exceptions.GlueException;
import com.github.mgramin.sqlglue.model.DBSchemaObject;
import com.github.mgramin.sqlglue.model.DBSchemaObjectType;
import com.github.mgramin.sqlglue.scanners.IObjectScanner;
import com.github.mgramin.sqlglue.scanners.AbstractObjectScanner;
import com.github.mgramin.sqlglue.uri.ObjURI;
import com.github.mgramin.sqlglue.util.sql.ISqlHelper;
import com.github.mgramin.sqlglue.util.template_engine.ITemplateEngine;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by mgramin on 31.10.2016.
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
    public Map<String, DBSchemaObject> scan(ObjURI objURI, DBSchemaObjectType type) throws GlueException {
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
                Map<String, Object> dataNew = new LinkedHashMap<>();
                for (Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {
                    if (!stringStringEntry.getKey().startsWith("@")) {
                        dataNew.put(stringStringEntry.getKey(), stringStringEntry.getValue());
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

                List<String> strings = (List<String>) (Object) new ArrayList<>(dataNew.values());
                object.objURI = new ObjURI(type.name, strings);
                object.type = type;
                objects.put(object.objURI.toString(), object);
            }

        } catch (Exception e) {
            throw new GlueException(e);
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