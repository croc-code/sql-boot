package com.github.mgramin.sqlglue.scanners.impl;

import com.github.mgramin.sqlglue.model.DBSchemaObject;
import com.github.mgramin.sqlglue.model.DBSchemaObjectType;
import com.github.mgramin.sqlglue.scanners.IObjectScanner;
import com.github.mgramin.sqlglue.scanners.AbstractObjectScanner;
import com.github.mgramin.sqlglue.uri.ObjURI;

import java.util.Map;

/**
 * Created by mgramin on 31.10.2016.
 */
public class FSObjectScanner extends AbstractObjectScanner implements IObjectScanner {

    @Override
    public Map<String, DBSchemaObject> scan(ObjURI objURI, DBSchemaObjectType type) {
        return null;
    }

}
