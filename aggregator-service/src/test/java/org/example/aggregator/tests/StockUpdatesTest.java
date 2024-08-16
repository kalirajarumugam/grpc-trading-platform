package org.example.aggregator.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.aggregator.dto.PriceUpdateDto;
import org.example.aggregator.tests.mockservice.StockMockService;
import org.example.common.Ticker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.annotation.DirtiesContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootTest(properties = {
        "grpc.server.port=-1",
        "grpc.server.in-process-name=integration-test",
        "grpc.client.stock-service.address=in-process:integration-test"
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockUpdatesTest {

    private static final Logger log = LoggerFactory.getLogger(StockUpdatesTest.class);
    private static final String STOCK_UPDATES_ENDPOINT = "http://localhost:%d/stock/updates";

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void stockUpdatesTest(){

        var url = STOCK_UPDATES_ENDPOINT.formatted(port);
        log.info("URL : {}", url);

        var list = this.restTemplate.execute(
                url,
                HttpMethod.GET,
                null,
                this::getResponse
        );
        Assertions.assertEquals(5, list.size());
        Assertions.assertEquals(Ticker.AMAZON.toString(), list.get(0).ticker());
        Assertions.assertEquals(1, list.get(0).price());
    }

    private List<PriceUpdateDto> getResponse(ClientHttpResponse clientHttpResponse){
        log.info("getResponse method");
        var list = new ArrayList<PriceUpdateDto>();
        try(var reader = new BufferedReader(new InputStreamReader(clientHttpResponse.getBody()))){
            String line;
            while(Objects.nonNull(line = reader.readLine())){
                if(!line.isEmpty()){
                    log.info(line);
                    var dto = mapper.readValue(line.substring("data:".length()), PriceUpdateDto.class);
                    list.add(dto);
                }
            }
        }catch (Exception e){
            log.error("streaming error", e);
        }
        return list;
    }

    @TestConfiguration
    static class TestConfig {

        @GrpcService
        public StockMockService stockMockService(){
            return new StockMockService();
        }

    }
}