package com.baro.auth.infra.oauth.naver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.naver")
public record NaverOAuthProperty(
        String clientId,
        String clientSecret,
        String signInAuthorizeUrl,
        String responseType,
        String redirectUri,
        String state,
        String grantType
) {
}
