package com.github.mgramin.sqlboot.script.aggregators.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBSchemaObject;
import com.github.mgramin.sqlboot.script.aggregators.AbstractAggregator;
import com.github.mgramin.sqlboot.script.aggregators.IAggregator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.mgramin.sqlboot.util.ZipHelper.compress;

/**
 * Created by mgramin on 17.12.2016.
 */
public class ZipAggregator extends AbstractAggregator implements IAggregator {

    private String zipFileName;

    public String getZipFileName() {
        return zipFileName;
    }

    public void setZipFileName(String zipFileName) {
        this.zipFileName = zipFileName;
    }

    @Override
    public byte[] aggregate(List<DBSchemaObject> objects) throws SqlBootException {
        Map<String, byte[]> files = new HashMap<>();
        for (DBSchemaObject o : objects) {
            if (o.getProp("file_name") != null && !o.getProp("file_name").isEmpty())
                files.put(o.getProp("file_name").toLowerCase(), o.ddl.getBytes());
        }
        return compress(files);
    }

}
