package org.example.paymentservice.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class YookassaFeignConfig {

    @Value("${payment.yookassa.shop-id}")
    private String shopId;

    @Value("${payment.yookassa.secret-key}")
    private String secretKey;

    @Bean
    public RequestInterceptor basicAuthRequestInterceptor() {
        return requestTemplate -> {
            String auth = shopId + ":" + secretKey;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            String authHeader = "Basic " + new String(encodedAuth);
            requestTemplate.header("Authorization", authHeader);
        };
    }
}

