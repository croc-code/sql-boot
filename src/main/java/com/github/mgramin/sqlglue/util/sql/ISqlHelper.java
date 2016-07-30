package com.github.mgramin.sqlglue.util.sql;

import java.util.List;
import java.util.Map;

/**
 * Created by maksim on 27.07.16.
 */
public interface ISqlHelper {

    List<Map<String, String>> select(String sql);

}
