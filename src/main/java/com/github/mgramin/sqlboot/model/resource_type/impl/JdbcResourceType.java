package com.github.mgramin.sqlboot.model.resource_type.impl;

import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.model.DbResourceThin;
import com.github.mgramin.sqlboot.model.IDbResourceCommand;
import com.github.mgramin.sqlboot.model.Uri;
import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import com.github.mgramin.sqlboot.tools.jdbc.Column;
import com.github.mgramin.sqlboot.tools.jdbc.DbObject;
import com.github.mgramin.sqlboot.tools.jdbc.Table;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.sql.DataSource;

/**
 * Created by MGramin on 12.07.2017.
 */
public class JdbcResourceType implements ResourceType {

    final private List<String> aliases;
    final private List<ResourceType> child;
    final private List<DbObject> dbObjects;

    public JdbcResourceType(DataSource dataSource, List<String> aliases) {
        this(dataSource, aliases, null);
    }

    public JdbcResourceType(DataSource dataSource, List<String> aliases, List<ResourceType> child) {
        this.aliases = aliases;
        this.child = child;
        dbObjects = new ArrayList<>();
        dbObjects.add(new Table(dataSource));
        dbObjects.add(new Column(dataSource));
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
        DbObject dbObject = dbObjects.stream().filter(o -> o.name().equals(uri.type())).findAny().orElse(null);
        try {
            List<Map<String, String>> maps = dbObject.get(uri.objects());
            for (Map<String, String> map : maps) {
                dbResourceList.add(new DbResourceThin("name", this, null, map));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbResourceList;
    }

}
