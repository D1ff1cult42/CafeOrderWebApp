package org.example.apigateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("API Gateway Service API")
                        .description("API документация для API Gateway сервиса, часть системы онлайн-заказа пиццы, способен отправлять агрегированные запросы к микросервисам.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("D1ff1cult42")
                                .email("orbitovkek@gmail.com")
                        )
                );
    }
}
