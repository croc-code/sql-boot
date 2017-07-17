package com.github.mgramin.sqlboot.tools.jdbc.wrappers;

import com.github.mgramin.sqlboot.tools.jdbc.DbObject;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by MGramin on 18.07.2017.
 */
public class FilterWrapper implements DbObject {

    private final DbObject dbObject;

    public FilterWrapper(DbObject dbObject) {
        this.dbObject = dbObject;
    }

    @Override
    public String name() {
        return dbObject.name();
    }

    @Override
    public List<Map<String, String>> read(List<String> params) throws SQLException {
        return null;
    }

}
