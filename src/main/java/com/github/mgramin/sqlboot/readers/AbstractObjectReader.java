package com.github.mgramin.sqlboot.readers;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.model.DBSchemaObjectType;
import com.github.mgramin.sqlboot.uri.ObjURI;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by mgramin on 03.11.2016.
 */
public abstract class AbstractObjectReader implements IDBObjectReader {

    public Map<String, DBSchemaObject> readr(ObjURI objURI, DBSchemaObjectType type) throws SqlBootException {
        Map<String, DBSchemaObject> objects = new LinkedHashMap<>(this.read(objURI, type));
        if (type.child != null) {
            for (DBSchemaObjectType childType : type.child) {
                objURI.setParams(null);
                objects.putAll(childType.readers.stream().findFirst().get().readr(objURI, childType));
            }
        }
        return objects;
    }

}