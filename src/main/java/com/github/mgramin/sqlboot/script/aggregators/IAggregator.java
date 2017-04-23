package com.github.mgramin.sqlboot.script.aggregators;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBResource;

import java.util.List;
import java.util.Map;

/**
 * Created by mgramin on 17.12.2016.
 */
public interface IAggregator {

    String getName();
    Boolean getIsDefault();
    Map<String, String> getHttpHeaders();

    byte[] aggregate(List<DBResource> objects) throws SqlBootException;

}
