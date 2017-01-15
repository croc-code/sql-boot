package com.github.mgramin.sqlboot.util.sql;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;

import java.util.List;
import java.util.Map;

/**
 * Created by maksim on 27.07.16.
 */
public interface ISqlHelper {

    List<Map<String, String>> select(String sql) throws SqlBootException;

    List<Map<String, String>> selectBatch(List<String> sql) throws SqlBootException;

}
