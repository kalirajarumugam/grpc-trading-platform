package org.example.aggregator.tests;

import net.devh.boot.grpc.server.service.GrpcService;
import org.example.aggregator.tests.mockservice.StockMockService;
import org.example.aggregator.tests.mockservice.UserMockService;
import org.example.user.UserInformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@SpringBootTest(
        properties = {
                "grpc.server.port=-1",
                "grpc.server.in-process-name=integration-test",
                "grpc.client.user-service.address=in-process:integration-test",
                "grpc.client.stock-service.address=in-process:integration-test"
        }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class UserTradeTest {

    private static final String USER_INFORMATION_ENDPOINT = "http://localhost:%d/user/%d";
    private static final String STOCK_INFORMATION_ENDPOINT = "http://localhost:%d/trade/%d";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void userInformationTest(){

        var url = USER_INFORMATION_ENDPOINT.formatted(port, 1);
        System.out.println("PORT ************* : " + url);
        var response = this.restTemplate.getForEntity(url, UserInformation.class);
        var res1 = restTemplate.getForObject(url, UserInformation.class);
        System.out.println("response ************* : " + response);
        System.out.println("response1  ************* : " + res1);

//        Assertions.assertEquals(200, response.getStatusCode().value());

        var user = response.getBody();

        System.out.println("response body ************* : " + user);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(1, user.getUserId());
        Assertions.assertEquals("integration-test", user.getName());
        Assertions.assertEquals(100, user.getBalance());

    }

    @TestConfiguration
    static class TestConfig{
        @GrpcService
        public StockMockService stockMockService(){
            return new StockMockService();
        }

        @GrpcService
        public UserMockService userMockService(){
            return new UserMockService();
        }

    }

}
