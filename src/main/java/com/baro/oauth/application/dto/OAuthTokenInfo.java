package com.baro.oauth.application.dto;

public record OAuthTokenInfo(
        String accessToken,
        String refreshToken,
        Integer expiresIn
) {
}
