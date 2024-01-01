package com.baro.auth.application.oauth.dto;

public record OAuthTokenInfo(
        String accessToken,
        String refreshToken,
        Integer expiresIn
) {
}
