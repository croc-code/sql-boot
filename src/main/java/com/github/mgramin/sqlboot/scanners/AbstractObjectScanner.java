package com.github.mgramin.sqlboot.scanners;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.model.DBSchemaObjectType;
import com.github.mgramin.sqlboot.uri.ObjURI;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by mgramin on 03.11.2016.
 */
public abstract class AbstractObjectScanner implements IObjectScanner {

    public Map<String, DBSchemaObject> scanr(ObjURI objURI, DBSchemaObjectType type) throws SqlBootException {
        Map<String, DBSchemaObject> objects = new LinkedHashMap<>();
        objects.putAll(this.scan(objURI, type));

        if (type.child != null) {
            for (DBSchemaObjectType childType : type.child) {
                objects.putAll(childType.scanners.stream().findFirst().get().scan(objURI, childType));
            }
        }
        return objects;
    }

}