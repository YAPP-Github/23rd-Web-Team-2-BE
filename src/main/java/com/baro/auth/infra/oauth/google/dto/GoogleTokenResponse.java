package com.baro.auth.infra.oauth.google.dto;

import com.baro.auth.application.oauth.dto.OAuthTokenInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleTokenResponse(
    String accessToken,
    String idToken,
    Integer expiresIn,
    String tokenType,
    String scope,
    String refreshToken
) {

    public OAuthTokenInfo toOAuthTokenInfo() {
        return new OAuthTokenInfo(accessToken, refreshToken, expiresIn);
    }
}
