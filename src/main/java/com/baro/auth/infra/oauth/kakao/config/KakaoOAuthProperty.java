package com.baro.auth.infra.oauth.kakao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoOAuthProperty(
        String clientId,
        String redirectUri,
        String responseType,
        String signInAuthorizeUrl,
        String[] scope,
        String grantType,
        String clientSecret
) {
}
