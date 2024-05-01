package com.baro.auth.infra.oauth.naver.dto;

import com.baro.auth.application.oauth.dto.OAuthTokenInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record NaverTokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Integer expiresIn
) {

    public OAuthTokenInfo toOAuthTokenInfo() {
        return new OAuthTokenInfo(accessToken, refreshToken, expiresIn);
    }
}
