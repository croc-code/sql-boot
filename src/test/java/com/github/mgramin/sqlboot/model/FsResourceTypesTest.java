package com.github.mgramin.sqlboot.model;

import java.util.List;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.resource.DbResource;
import com.github.mgramin.sqlboot.resource_type.ResourceType;
import com.github.mgramin.sqlboot.resource_types.impl.FsResourceTypes;
import com.github.mgramin.sqlboot.uri.Uri;
import com.github.mgramin.sqlboot.uri.impl.DbUri;
import com.github.mgramin.sqlboot.uri.wrappers.SqlPlaceholdersWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static java.util.Arrays.asList;

/**
 * Created by MGramin on 11.07.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class FsResourceTypesTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void load() throws Exception {
        List<ResourceType> types = new FsResourceTypes(dataSource).load();

        ResourceType columnType = types.stream()
                .filter(v -> v.name().equals("column"))
                .findAny().get();

        final Uri uri = new SqlPlaceholdersWrapper(
                new DbUri(columnType.name(), asList("MAIN_SCHEMA", "CIT*", "C*")));

        final List<DbResource> columns = columnType.read(uri);
        for (final DbResource column : columns) {
            System.out.println(column.dbUri());
        }

    }

}