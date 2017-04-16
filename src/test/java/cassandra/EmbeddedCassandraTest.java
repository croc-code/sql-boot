package cassandra;

import org.junit.Test;


/**
 * Created by mgramin on 20.12.2016.
 */
public class EmbeddedCassandraTest {

   /* @Rule
    public CassandraCQLUnit cassandraCQLUnit = new CassandraCQLUnit(new ClassPathCQLDataSet("simple.cql","test_ks"));
    */
    @Test
    public void test() {
        String clusterName = "TestCluster";
        String host = "localhost:9771";
        /*Cluster cluster = HFactory.getOrCreateCluster(clusterName, host);
        Keyspace keyspace = HFactory.createKeyspace("beautifulKeyspaceName", cluster);*/
    }

}
