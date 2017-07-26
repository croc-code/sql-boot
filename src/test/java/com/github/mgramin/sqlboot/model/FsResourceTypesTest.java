package com.github.mgramin.sqlboot.model;

import java.util.List;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.resource_type.ResourceType;
import com.github.mgramin.sqlboot.resource_types.ResourceTypes;
import com.github.mgramin.sqlboot.resource_types.impl.FsResourceTypes;
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
        ResourceTypes types = new FsResourceTypes(dataSource);
        List<ResourceType> load = types.load();

        ResourceType resourceType = load.get(0);

        DbUri uri = new DbUri("schema", asList("%", "%"));
        List<DbResource> dbResources = resourceType.read(uri, null, null);
        for (DbResource dbResource : dbResources) {
            System.out.println(dbResource.dbUri());
        }
    }

}