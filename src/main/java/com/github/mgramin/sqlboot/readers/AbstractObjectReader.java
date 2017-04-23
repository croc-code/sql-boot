package com.github.mgramin.sqlboot.readers;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBResource;
import com.github.mgramin.sqlboot.model.DBResourceType;
import com.github.mgramin.sqlboot.uri.ObjURI;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractObjectReader implements IDBObjectReader {

    public Map<String, DBResource> readr(ObjURI objURI, DBResourceType type) throws SqlBootException {
        Map<String, DBResource> objects = new LinkedHashMap<>(this.read(objURI, type));
        if (type.child != null) {
            for (DBResourceType childType : type.child) {
                objURI.setParams(null);
                childType.readers
                    .stream()
                    .findFirst()
                    .ifPresent(r -> objects.putAll(r.readr(objURI, childType)));
            }
        }
        return objects;
    }

}