package com.baro.oauth.infra.kakao.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoOAuthProperty(
        String clientId,
        String redirectUri,
        String responseType,
        String[] scope,
        String grantType
) {
}
