package com.github.mgramin.sqlglue.scanners;

import com.github.mgramin.sqlglue.exceptions.GlueException;
import com.github.mgramin.sqlglue.model.DBSchemaObject;
import com.github.mgramin.sqlglue.model.DBSchemaObjectType;
import com.github.mgramin.sqlglue.uri.ObjURI;

import java.util.Map;

/**
 * Created by mgramin on 31.10.2016.
 */
public interface IObjectScanner {

    Map<String, DBSchemaObject> scan(ObjURI objURI, DBSchemaObjectType type) throws GlueException;

    // TODO move to top ??
    Map<String, DBSchemaObject> scanr(ObjURI objURI, DBSchemaObjectType type) throws GlueException;

}
