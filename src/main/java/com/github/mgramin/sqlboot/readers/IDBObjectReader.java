package com.github.mgramin.sqlboot.readers;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import com.github.mgramin.sqlboot.model.DBResource;
import com.github.mgramin.sqlboot.model.DBResourceType;
import com.github.mgramin.sqlboot.uri.ObjURI;

import java.util.Map;

/**
 * Created by mgramin on 31.10.2016.
 */
public interface IDBObjectReader {

    Map<String, DBResource> read(ObjURI objURI, DBResourceType type) throws SqlBootException;

    // TODO move to top ??
    Map<String, DBResource> readr(ObjURI objURI, DBResourceType type) throws SqlBootException;

}
