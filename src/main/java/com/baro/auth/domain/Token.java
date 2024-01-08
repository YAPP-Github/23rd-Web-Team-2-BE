package com.baro.auth.domain;

public record Token(
        String accessToken,
        String refreshToken
) {
}
