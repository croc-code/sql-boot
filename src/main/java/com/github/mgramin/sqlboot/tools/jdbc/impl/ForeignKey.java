package com.github.mgramin.sqlboot.tools.jdbc.impl;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObject;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import lombok.ToString;

/**
 * Created by MGramin on 13.07.2017.
 */
@ToString
public class ForeignKey extends AbstractJdbcObjectType implements JdbcDbObjectType {

    private final DataSource dataSource;

    public ForeignKey(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String name() {
        return "fk";
    }

    @Override
    public List<JdbcDbObject> read(List<String> params) throws SQLException {
        /*ResultSet foreignKeys = dataSource.getConnection().getMetaData().
            getImportedKeys(null, params.get(0), params.get(1));
        return customResultSet.toMap(foreignKeys);*/
        return null;
    }

}
