package com.baro.auth.infra.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt.token")
public record JwtProperty(
        String bearerType,
        String accessSecretKey,
        String refreshSecretKey,
        Long accessTokenExpireTime,
        Long refreshTokenExpireTime
) {
}
