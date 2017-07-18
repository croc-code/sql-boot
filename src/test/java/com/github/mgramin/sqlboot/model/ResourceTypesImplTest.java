package com.github.mgramin.sqlboot.model;

import javax.sql.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by MGramin on 11.07.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"/test_config.xml"})
public class ResourceTypesImplTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void load() throws Exception {
        /*ResourceTypes types = new ResourceTypesImpl(dataSource);
        List<ResourceType> load = types.load();

        DbUri uri = new DbUri("table", asList("%", "%"));
        ResourceType resourceType = load.get(0);
        resourceType.read(uri, null, null);*/

        /*for (ResourceType resourceType : load) {
            System.out.println(resourceType.aliases().get(0));
        }*/
    }

}