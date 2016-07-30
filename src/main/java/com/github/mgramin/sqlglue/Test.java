package com.github.mgramin.sqlglue;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * Created by maksim on 27.07.16.
 */
public class Test {

    public static void main(String[] args) {
        System.out.println("!");


        Cluster cluster;
        Session session;

        // Connect to the cluster and keyspace "demo"
        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect("test");



    }

}
