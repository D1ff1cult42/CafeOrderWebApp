package org.example.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

@SpringBootTest(
        classes = {ApiGatewayApplicationTests.TestConfig.class},
        properties = {
                "order.service.url=http://localhost:8082",
                "payment.service.url=http://localhost:8083",
                "menu.service.url=http://localhost:8081"
        }
)
class ApiGatewayApplicationTests {

    @Configuration
    static class TestConfig {
    }

    @Test
    void contextLoads() {
    }

}
