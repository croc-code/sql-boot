package com.github.mgramin.sqlboot.rest;

import com.github.mgramin.sqlboot.model.DBSchemaObject;

import java.util.List;

/**
 * Created by mgramin on 17.12.2016.
 */
public class ZipAggregator implements IAggregator {

    @Override
    public byte[] aggregate(List<DBSchemaObject> objects) {
        return new byte[0];
    }

}
