package com.github.mgramin.sqlboot.scanners.impl;

import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.model.DBSchemaObjectType;
import com.github.mgramin.sqlboot.scanners.AbstractObjectScanner;
import com.github.mgramin.sqlboot.scanners.IObjectScanner;
import com.github.mgramin.sqlboot.uri.ObjURI;

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
