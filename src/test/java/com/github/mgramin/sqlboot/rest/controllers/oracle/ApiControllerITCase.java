package com.github.mgramin.sqlboot.rest.controllers.oracle;

import com.github.mgramin.sqlboot.rest.RestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"db_dev_user=system", "db_dev_password=oracle",
"db_dev_url=jdbc:oracle:thin:@127.0.0.1:1521:XE"})
@ActiveProfiles("oracle")
//@ContextConfiguration(initializers = ApiControllerITCase.Initializer.class)
public class ApiControllerITCase {

    @Autowired
    private TestRestTemplate client;

    /*@ClassRule
    public static GenericContainer oracle = new GenericContainer("sath89/oracle-xe-11g:latest")
            .withExposedPorts(1521);


    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            String xe = format("db_dev_url=jdbc:oracle:thin:@%s:%s:%s",
                    oracle.getContainerIpAddress(), oracle.getMappedPort(1521), "XE");
            System.out.println(xe);
            EnvironmentTestUtils.addEnvironment(
                    "testcontainers", configurableApplicationContext.getEnvironment(), xe);
        }
    }*/

    @Test
    public void getTextDdl() throws Exception {
//        Thread.sleep(40000);
        callRest("table/hr/-");
        callRest("table/hr");
//        callRestAndValidate("t/public.basketball_team_stats/d", "postgres/t_public.basketball_team_stats_drop");
//        callRestAndValidate("t/public.basketball_team_stats/-", "postgres/t_public.basketball_team_stats_drop");
    }


    private void callRest(String uri) {
        ResponseEntity<String> response = client.getForEntity("/api/" + uri, String.class);
        assertEquals(response.getStatusCodeValue(), 200);
        System.out.println(response.getBody());
    }

    private void callRestAndValidate(String uri, String file) throws IOException, InterruptedException {
        ResponseEntity<String> response = client.getForEntity("/api/" + uri, String.class);
        assertEquals(response.getStatusCodeValue(), 200);
        String s = response.getBody();
        String s1 = readFileToString(new File(getClass().getClassLoader().getResource(file).getFile()), "UTF-8");
        System.out.println(s);
        assertEquals(s1, s.trim());
    }

}