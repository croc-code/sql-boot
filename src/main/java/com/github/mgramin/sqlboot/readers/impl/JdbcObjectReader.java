package com.github.mgramin.sqlboot.readers.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.model.DBSchemaObjectType;
import com.github.mgramin.sqlboot.readers.AbstractObjectReader;
import com.github.mgramin.sqlboot.readers.IDBObjectReader;
import com.github.mgramin.sqlboot.uri.ObjURI;

import java.util.Map;

/**
 * Created by MGramin on 24.11.2016.
 */
public class JdbcObjectReader extends AbstractObjectReader implements IDBObjectReader {

    @Override
    public Map<String, DBSchemaObject> read(ObjURI objURI, DBSchemaObjectType type) throws SqlBootException {
        return null;
    }

}