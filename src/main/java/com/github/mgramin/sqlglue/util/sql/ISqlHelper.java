package com.github.mgramin.sqlglue.util.sql;

import com.github.mgramin.sqlglue.exceptions.GlueException;

import java.util.List;
import java.util.Map;

/**
 * Created by maksim on 27.07.16.
 */
public interface ISqlHelper {

    List<Map<String, String>> select(String sql) throws GlueException;

}
