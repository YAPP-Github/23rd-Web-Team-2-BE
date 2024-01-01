package com.baro.auth.infra.oauth.google.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.google")
public record GoogleOAuthProperty(
        String clientId,
        String clientSecret,
        String signInAuthorizeUrl,
        String responseType,
        String tokenUri,
        String redirectUri,
        String[] scope
) {
}
