package com.github.mgramin.sqlboot.util.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.ToString;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by MGramin on 16.02.2017.
 */
@ConfigurationProperties()
@ToString
public class DbConnectionManager {

    private Map<String, DataSource> dataSources;


    public Map<String, DataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(
        Map<String, DataSource> dataSources) {
        this.dataSources = dataSources;
    }


    private List<DbConnection> connections = new ArrayList<>();

    public List<DbConnection> getConnections() {
        return connections;
    }

    public void setConnections(List<DbConnection> connections) {
        this.connections = connections;
    }

    public static class DbConnection {

        private String name;
        private String description;
        private Boolean isDefault;
        private String driver;
        private String url;
        private String user;
        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Boolean getDefault() {
            return isDefault;
        }

        public void setDefault(Boolean aDefault) {
            isDefault = aDefault;
        }

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

    public void init() {
        System.out.println("!!!!!!!!!!!!!!!!!");
        for (DbConnection connection : connections) {
            System.out.println("*********************");
            DataSource dataSource = new DataSource();
            dataSource.setDriverClassName(connection.driver);
            dataSource.setUrl(connection.url);
            dataSource.setUsername(connection.user);
            dataSource.setPassword(connection.password);
            dataSource.setValidationQuery("select 1");
            dataSource.setInitialSize(5);
            dataSource.setMaxActive(10);
            dataSource.setMaxIdle(5);
            dataSource.setMinIdle(2);

            try {
                Connection connection1 = dataSource.getConnection();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

        }
    }

}
