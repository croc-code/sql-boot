package com.github.mgramin.sqlboot.rest;

import com.github.mgramin.sqlboot.model.DBSchemaObject;

import java.util.List;

/**
 * Created by mgramin on 17.12.2016.
 */
public class TextAggregator implements IAggregator {

    @Override
    public byte[] aggregate(List<DBSchemaObject> objects) {
        StringBuilder builder = new StringBuilder();
        for (DBSchemaObject object : objects) {
            builder.append(object.ddl).append("\n");
        }
        return builder.toString().getBytes();
    }

}
