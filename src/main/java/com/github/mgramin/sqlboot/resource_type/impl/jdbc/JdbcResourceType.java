package com.github.mgramin.sqlboot.resource_type.impl.jdbc;

import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.model.DbResourceThin;
import com.github.mgramin.sqlboot.model.IDbResourceCommand;
import com.github.mgramin.sqlboot.model.Uri;
import com.github.mgramin.sqlboot.resource_type.ResourceType;
import com.github.mgramin.sqlboot.tools.jdbc.JdbcDbObjectType;
import com.github.mgramin.sqlboot.tools.jdbc.impl.Column;
import com.github.mgramin.sqlboot.tools.jdbc.impl.Table;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

/**
 * Created by MGramin on 12.07.2017.
 */
public class JdbcResourceType implements ResourceType {

    final private List<String> aliases;
    final private List<ResourceType> child;
    final private List<JdbcDbObjectType> jdbcDbObjectTypes;

    public JdbcResourceType(DataSource dataSource, List<String> aliases) {
        this(dataSource, aliases, null);
    }

    public JdbcResourceType(DataSource dataSource, List<String> aliases, List<ResourceType> child, JdbcDbObjectType jdbcDbObjectType) {
        this(dataSource, aliases, null);
        // TODO
    }

    @Deprecated
    public JdbcResourceType(DataSource dataSource, List<String> aliases, List<ResourceType> child) {
        this.aliases = aliases;
        this.child = child;
        jdbcDbObjectTypes = new ArrayList<>();
        jdbcDbObjectTypes.add(new Table(dataSource));
        jdbcDbObjectTypes.add(new Column(dataSource));
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
        JdbcDbObjectType jdbcDbObjectType = jdbcDbObjectTypes.stream().filter(o -> o.name().equals(uri.type())).findAny()
            .orElse(null);
        try {
            List<Map<String, String>> maps = jdbcDbObjectType.read(uri.objects());
            for (Map<String, String> map : maps) {
                dbResourceList.add(new DbResourceThin("name", this, null, map));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbResourceList;
    }

}
