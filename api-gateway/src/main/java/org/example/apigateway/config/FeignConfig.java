package org.example.apigateway.config;

import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.openfeign.support.FeignHttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Encoder feignEncoder(ObjectProvider<FeignHttpMessageConverters> messageConverters) {
        return new SpringEncoder(messageConverters);
    }

    @Bean
    public Decoder feignDecoder(ObjectProvider<FeignHttpMessageConverters> messageConverters) {
        return new SpringDecoder(messageConverters);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
