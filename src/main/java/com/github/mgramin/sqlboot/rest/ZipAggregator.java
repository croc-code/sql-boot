package com.github.mgramin.sqlboot.rest;

import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.util.ZipHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mgramin on 17.12.2016.
 */
public class ZipAggregator implements IAggregator {

    @Override
    public byte[] aggregate(List<DBSchemaObject> objects) {

        Map<String, byte[]> files = new HashMap<>();

        for (DBSchemaObject object : objects) {
            files.put(object.name + ".sql", object.ddl.getBytes());
        }


        return new ZipHelper().create(files);
    }

}
