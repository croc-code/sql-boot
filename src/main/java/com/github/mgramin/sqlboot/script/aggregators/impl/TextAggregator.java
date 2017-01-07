package com.github.mgramin.sqlboot.script.aggregators.impl;

import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.script.aggregators.IAggregator;

import java.util.List;

/**
 * Created by mgramin on 17.12.2016.
 */
public class TextAggregator implements IAggregator {

    @Override
    public byte[] aggregate(List<DBSchemaObject> objects) {
        StringBuilder builder = new StringBuilder();
        for (DBSchemaObject o : objects) builder.append(o.ddl).append("\n");
        return builder.toString().getBytes();
    }

}
