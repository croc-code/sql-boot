/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Maksim Gramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.rest.controllers.postgres;

import com.github.mgramin.sqlboot.rest.RestRunner;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"db_dev_user=postgres", "db_dev_password=postgres"})
@ActiveProfiles("postgres")
@ContextConfiguration(initializers = ApiControllerITCase.Initializer.class)
public class ApiControllerITCase {

    @Autowired
    private TestRestTemplate client;

    public static final int POSTGRES_PORT = 5432;
    @ClassRule
    public static GenericContainer postgres = new GenericContainer("mgramin/postgres-sport:latest")
            .withExposedPorts(POSTGRES_PORT);


    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            EnvironmentTestUtils.addEnvironment("testcontainers", configurableApplicationContext.getEnvironment(),
                    format("db_dev_url=jdbc:postgresql://%s:%s/%s",
                            postgres.getContainerIpAddress(), postgres.getMappedPort(POSTGRES_PORT), "postgres"));
        }
    }

    @Test
    public void getTextDdl() throws Exception {
        Thread.sleep(2000);
        callRestAndValidate("t/public.basketball_team_stats/drop", "postgres/t_public.basketball_team_stats_drop");
        callRestAndValidate("t/public.basketball_team_stats/d", "postgres/t_public.basketball_team_stats_drop");
        callRestAndValidate("t/public.basketball_team_stats/-", "postgres/t_public.basketball_team_stats_drop");
    }

    @Test
    public void getOneTable() throws Exception {
        Thread.sleep(2000);
        callRestAndValidate("t/public.basketball_team_stats", "postgres/t_public.basketball_team_stats_drop");
    }

    @Test
    public void getOneTableColumns() throws Exception {
        Thread.sleep(2000);
        callRestAndValidate("c/public.basketball_team_stats", "postgres/t_public.basketball_team_stats_drop");
    }

/*    @Test
    public void getTableJsonData() throws IOException, InterruptedException {
        Thread.sleep(2000);
        callRestAndValidate("data/public.participants_events?type=json", null);
    }*/

    private void callRestAndValidate(String uri, String file) throws IOException, InterruptedException {
        ResponseEntity<String> response = client.getForEntity("/api/" + uri, String.class);
        assertEquals(response.getStatusCodeValue(), 200);
        String s = response.getBody();
//        String s1 = readFileToString(new File(getClass().getClassLoader().getResource(file).getFile()), "UTF-8");
        System.out.println(s);
//        assertEquals(s1, s.trim());
    }

}