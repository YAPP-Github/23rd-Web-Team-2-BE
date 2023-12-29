package com.baro.oauth.infra.naver.dto;

import com.baro.oauth.application.dto.OAuthTokenInfo;

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
