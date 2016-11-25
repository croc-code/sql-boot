package com.github.mgramin.sqlglue.scanners.impl;

import com.github.mgramin.sqlglue.exceptions.GlueException;
import com.github.mgramin.sqlglue.model.DBSchemaObject;
import com.github.mgramin.sqlglue.model.DBSchemaObjectType;
import com.github.mgramin.sqlglue.scanners.AbstractObjectScanner;
import com.github.mgramin.sqlglue.scanners.IObjectScanner;
import com.github.mgramin.sqlglue.uri.ObjURI;

import java.util.Map;

/**
 * Created by MGramin on 24.11.2016.
 */
public class JdbcObjectScanner extends AbstractObjectScanner implements IObjectScanner {

    @Override
    public Map<String, DBSchemaObject> scan(ObjURI objURI, DBSchemaObjectType type) throws GlueException {
        return null;
    }

}