package com.github.mgramin.sqlboot.scanners.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.model.DBSchemaObjectType;
import com.github.mgramin.sqlboot.scanners.AbstractObjectScanner;
import com.github.mgramin.sqlboot.scanners.IObjectScanner;
import com.github.mgramin.sqlboot.uri.ObjURI;

import java.util.Map;

/**
 * Created by MGramin on 24.11.2016.
 */
public class JdbcObjectScanner extends AbstractObjectScanner implements IObjectScanner {

    @Override
    public Map<String, DBSchemaObject> scan(ObjURI objURI, DBSchemaObjectType type) throws SqlBootException {
        return null;
    }

}