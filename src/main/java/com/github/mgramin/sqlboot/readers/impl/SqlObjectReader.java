package com.github.mgramin.sqlboot.readers.impl;

import static java.util.stream.Collectors.toMap;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBResource;
import com.github.mgramin.sqlboot.model.DBResourceType;
import com.github.mgramin.sqlboot.readers.AbstractObjectReader;
import com.github.mgramin.sqlboot.readers.IDBObjectReader;
import com.github.mgramin.sqlboot.uri.ObjURI;
import com.github.mgramin.sqlboot.util.sql.ISqlHelper;
import com.github.mgramin.sqlboot.util.template_engine.TemplateEngine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.ToString;
import org.apache.log4j.Logger;

/**
 * Custom-SQL db object reader
 */
@ToString
public class SqlObjectReader extends AbstractObjectReader implements IDBObjectReader {

    private final static Logger logger = Logger.getLogger(SqlObjectReader.class);

    public ISqlHelper sqlHelper;
    public String sql;
    public TemplateEngine templateEngine; // TODO move to decorator ?
    public String prepareSql; // TODO move to decorator ?

    public SqlObjectReader() {
    }

    public SqlObjectReader(ISqlHelper sqlHelper, TemplateEngine templateEngine, String sql, String prepareSql) {
        this.sqlHelper = sqlHelper;
        this.templateEngine = templateEngine;
        this.sql = sql;
        this.prepareSql = prepareSql;
    }

    public SqlObjectReader(ISqlHelper sqlHelper, TemplateEngine templateEngine, String sql) {
        this.sqlHelper = sqlHelper;
        this.templateEngine = templateEngine;
        this.sql = sql;
    }

    @Override
    public Map<String, DBResource> read(ObjURI objURI, DBResourceType type) throws SqlBootException {
        List<String> list = objURI.getObjects();

        Map<String, DBResource> objects = new LinkedHashMap<>();
        try {
            Map<String, Object> data = new HashMap();
            int i = 0;
            templateEngine.setTemplate(sql);
            for (String s : templateEngine.getAllProperties()) {
                try {
                    data.put(s, list.get(i++));
                } catch (Throwable t) {
                    data.put(s, '%');
                }
            }

            templateEngine.setTemplate(sql);
            String prepareSQL = templateEngine.process(data);
            logger.debug(prepareSQL);

            List<Map<String, String>> select = sqlHelper.select(prepareSQL);
            for (Map<String, String> stringStringMap : select) {
                DBResource object = new DBResource();
                object.paths = stringStringMap;
                List<String> objectsForUri = new ArrayList<>();
                for (Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {
                    if (!stringStringEntry.getKey().startsWith("@")) {
                        objectsForUri.add(stringStringEntry.getValue());
                        object.name = stringStringEntry.getValue();
                        object.addProperty(stringStringEntry.getKey(), stringStringEntry.getValue());
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
                logger.debug("find object " + object.objURI.toString());
            }

        } catch (Exception e) {
            throw new SqlBootException(e);
        }

        if (objURI.getParams() != null) {
            Map<String, String> filtersParam = objURI.getParams().entrySet().stream().filter(p ->
                !p.getKey().equalsIgnoreCase("type"))
                .collect(toMap(p -> p.getKey(), p -> p.getValue()));

            for (Entry<String, String> param : filtersParam.entrySet()) {
                if (param.getKey().startsWith("@")) {
                    objects = objects.entrySet().stream().filter(
                    o -> o.getValue().getHeaders().getProperty(param.getKey().substring(1))
                        .contains(param.getValue()))
                    .collect(toMap(o -> o.getKey(), o -> o.getValue()));
                }
            }
        }

        return objects;
    }

    public void setSqlHelper(ISqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
    }

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setPrepareSql(String prepareSql) {
        this.prepareSql = prepareSql;
    }

}