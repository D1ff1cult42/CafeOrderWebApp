package org.example.orderservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Service")
                        .description("API документация для работы с заказами")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("D1ff1cult42")
                                .email("orbitovkek@gmail.com")
                ));
    }
}
