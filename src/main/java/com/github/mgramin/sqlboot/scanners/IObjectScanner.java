package com.github.mgramin.sqlboot.scanners;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.model.DBSchemaObjectType;
import com.github.mgramin.sqlboot.uri.ObjURI;

import java.util.Map;

/**
 * Created by mgramin on 31.10.2016.
 */
public interface IObjectScanner {

    Map<String, DBSchemaObject> scan(ObjURI objURI, DBSchemaObjectType type) throws SqlBootException;

    // TODO move to top ??
    Map<String, DBSchemaObject> scanr(ObjURI objURI, DBSchemaObjectType type) throws SqlBootException;

}
