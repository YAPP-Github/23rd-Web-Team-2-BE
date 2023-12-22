package com.baro.oauth.infra.kakao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoOAuthProperty(
        String clientId,
        String redirectUrl,
        String responseType,
        String signInAuthorizeUrl,
        String[] scope,
        String grantType,
        String clientSecret
) {
}
