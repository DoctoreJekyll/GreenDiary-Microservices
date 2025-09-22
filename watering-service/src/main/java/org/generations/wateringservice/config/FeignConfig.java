package org.generations.wateringservice.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor feignAuthInterceptor() {
        return requestTemplate -> {
            var attrs = RequestContextHolder.getRequestAttributes();
            if (attrs instanceof ServletRequestAttributes sra) {
                String auth = sra.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
                if (auth != null) {
                    requestTemplate.header(HttpHeaders.AUTHORIZATION, auth);
                }
            }
        };
    }

    @Bean
    public org.springframework.web.context.request.RequestContextListener requestContextListener() {
        return new org.springframework.web.context.request.RequestContextListener();
    }

    @Bean
    public feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }
}
