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

package com.github.mgramin.sqlboot.rest.controllers.oracle;

import java.io.File;
import java.io.IOException;
import com.github.mgramin.sqlboot.tools.rest.RestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
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