package com.github.mgramin.sqlboot.readers.impl;

import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.model.DBSchemaObjectType;
import com.github.mgramin.sqlboot.readers.AbstractObjectReader;
import com.github.mgramin.sqlboot.readers.IDBObjectReader;
import com.github.mgramin.sqlboot.uri.ObjURI;

import java.util.Map;

/**
 * Created by mgramin on 31.10.2016.
 */
public class FSObjectReader extends AbstractObjectReader implements IDBObjectReader {

    @Override
    public Map<String, DBSchemaObject> read(ObjURI objURI, DBSchemaObjectType type) {
        return null;
    }

}
