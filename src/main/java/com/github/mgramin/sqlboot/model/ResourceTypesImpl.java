package com.github.mgramin.sqlboot.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import com.github.mgramin.sqlboot.resource_type.ResourceType;
import com.github.mgramin.sqlboot.resource_type.impl.jdbc.JdbcResourceType;
import static java.util.Collections.singletonList;

/**
 * Created by MGramin on 11.07.2017.
 */
public class ResourceTypesImpl implements ResourceTypes {

    final private DataSource dataSource;

    public ResourceTypesImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<ResourceType> load() {
        List<ResourceType> walk = walk("/home/maksim/src/sql-boot/src/main/resources/conf/h2/database");
        return walk;
    }

    private List<ResourceType> walk(final String path) {
        File[] list = new File(path).listFiles();
        if (list == null) return null;
        List<ResourceType> listMain = new ArrayList<>();
        for (File f : list) {
            if (f.isDirectory()) {
                List<ResourceType> walkList = null;
                walkList = walk(f.getAbsolutePath());
                JdbcResourceType jdbcResourceType = new JdbcResourceType(dataSource, singletonList(f.getName()), walkList);
                listMain.add(jdbcResourceType);
            }
        }
        return listMain;
    }

}
