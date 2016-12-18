package com.github.mgramin.sqlboot.readers;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.model.DBSchemaObjectType;
import com.github.mgramin.sqlboot.uri.ObjURI;

import java.util.Map;

/**
 * Created by mgramin on 31.10.2016.
 */
public interface IDBObjectReader {

    Map<String, DBSchemaObject> read(ObjURI objURI, DBSchemaObjectType type) throws SqlBootException;

    // TODO move to top ??
    Map<String, DBSchemaObject> readr(ObjURI objURI, DBSchemaObjectType type) throws SqlBootException;

}
