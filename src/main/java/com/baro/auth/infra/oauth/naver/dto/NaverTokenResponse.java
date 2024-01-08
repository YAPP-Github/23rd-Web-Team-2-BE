package com.baro.auth.infra.oauth.naver.dto;

import com.baro.auth.application.oauth.dto.OAuthTokenInfo;

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
