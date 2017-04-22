package com.github.mgramin.sqlboot.rest.controllers.postgres;

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
@TestPropertySource(properties = {
        "management.port=0", "db_dev_url=jdbc:postgresql://localhost:9876/postgres",
        "db_dev_user=postgres", "db_dev_password=postgres" })
@ActiveProfiles("postgres")
public class DdlControllerITCase {

    @Autowired
    private TestRestTemplate client;


    @Test
    public void getTextDdl() throws Exception {
        callRestAndValidate("t/public.basketball_team_stats/drop", "postgres/t_public.basketball_team_stats_drop");
        callRestAndValidate("t/public.basketball_team_stats/d", "postgres/t_public.basketball_team_stats_drop");
        callRestAndValidate("t/public.basketball_team_stats/-", "postgres/t_public.basketball_team_stats_drop");
    }

    private void callRestAndValidate(String uri, String file) throws IOException {
        ResponseEntity<String> response = client.getForEntity("/api/" + uri, String.class);
        assertEquals(response.getStatusCodeValue(), 200);
        String s = response.getBody();
        String s1 = readFileToString(new File(getClass().getClassLoader().getResource(file).getFile()), "UTF-8");
        assertEquals(s1, s.trim());
    }

}