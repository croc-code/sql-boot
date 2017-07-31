package com.github.mgramin.sqlboot.resource_type.impl.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.model.DbResourceThin;
import com.github.mgramin.sqlboot.resource_type.ResourceType;
import com.github.mgramin.sqlboot.sql.ISqlHelper;
import com.github.mgramin.sqlboot.uri.Uri;
import com.github.mgramin.sqlboot.uri.impl.DbUri;
import org.apache.log4j.Logger;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.strip;

/**
 * Created by MGramin on 12.07.2017.
 */
public class SqlResourceType implements ResourceType {

    private final static Logger logger = Logger.getLogger(SqlResourceType.class);

    private final ISqlHelper sqlHelper;
    private final List<String> aliases;
    private final List<ResourceType> child;
    private final String sql;

    public SqlResourceType(ISqlHelper sqlHelper, List<String> aliases, String sql) {
        this(sqlHelper, aliases, null, sql);
    }

    public SqlResourceType(ISqlHelper sqlHelper, List<String> aliases, List<ResourceType> child, String sql) {
        this.sqlHelper = sqlHelper;
        this.aliases = aliases;
        this.child = child;
        this.sql = sql;
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
    public List<DbResource> read(Uri uri) throws BootException {

        final List<DbResource> objects = new ArrayList<>();
        final String sql = "select 'create table'";

        logger.debug(sql);

        final List<Map<String, String>> select = sqlHelper.select(sql);


        for (Map<String, String> row : select) {
            final List<String> objectsForUri = new ArrayList<>();
            final HashMap<String, String> objectHeaders = new LinkedHashMap<>();
            for (Map.Entry<String, String> column : row.entrySet()) {
                if (!column.getKey().startsWith("@")) {
                    objectsForUri.add(column.getValue());
                }
                objectHeaders.put(strip(column.getKey(), "@"), ofNullable(column.getValue()).orElse(""));
            }
            final String objectName = objectsForUri.get(objectsForUri.size() - 1);
            final DbResource object = new DbResourceThin(objectName, this, new DbUri(this.name(), objectsForUri),
                    objectHeaders);
            logger.debug("find object " + object.dbUri().toString());
            objects.add(object);
        }
        return objects;

    }

}
