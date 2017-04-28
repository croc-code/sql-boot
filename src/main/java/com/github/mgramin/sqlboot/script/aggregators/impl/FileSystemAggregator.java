package com.github.mgramin.sqlboot.script.aggregators.impl;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBResource;
import com.github.mgramin.sqlboot.script.aggregators.AbstractAggregator;
import com.github.mgramin.sqlboot.script.aggregators.IAggregator;
import java.util.List;

/**
 * Created by MGramin on 27.04.2017.
 */
public class FileSystemAggregator extends AbstractAggregator implements IAggregator {

    @Override
    public byte[] aggregate(List<DBResource> objects) throws SqlBootException {
        return new byte[0];
    }

}
