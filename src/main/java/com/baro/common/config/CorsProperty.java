package com.baro.common.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cors")
public record CorsProperty(
        String[] allowedOrigins
) {
}
