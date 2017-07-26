package com.github.mgramin.sqlboot.resource_type.impl.jdbc;

import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.model.DbResourceThin;
import com.github.mgramin.sqlboot.model.IDbResourceCommand;
import com.github.mgramin.sqlboot.model.Uri;
import com.github.mgramin.sqlboot.resource_type.ResourceType;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import lombok.ToString;

/**
 * Created by MGramin on 12.07.2017.
 */
@ToString
public class JdbcResourceType implements ResourceType {

    final private List<String> aliases;
    final private List<ResourceType> child;
    final private JdbcDbObjectType jdbcDbObjectType;

    public JdbcResourceType(List<String> aliases, JdbcDbObjectType jdbcDbObjectType) {
        this(aliases, null, jdbcDbObjectType);
    }

    public JdbcResourceType(List<String> aliases, List<ResourceType> child, JdbcDbObjectType jdbcDbObjectType) {
        this.aliases = aliases;
        this.child = child;
        this.jdbcDbObjectType = jdbcDbObjectType;
    }

    @Override
    public String name() {
        return aliases.get(0);
    }

    @Override
    public List<String> aliases() {
        return aliases;
    }

    @Override
    public List<ActionGenerator> generators() {
        return null;
    }

    @Override
    public List<DbResource> read(Uri uri, IDbResourceCommand command, String aggregatorName)
            throws BootException {
        List<DbResource> dbResourceList = new ArrayList<>();
        try {
            List<JdbcDbObject> list = jdbcDbObjectType.read(uri.objects());
            for (JdbcDbObject l : list) {
                dbResourceList.add(new DbResourceThin(l.name(), this, uri, l.properties()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbResourceList;
    }

}
