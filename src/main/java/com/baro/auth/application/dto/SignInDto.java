package com.baro.auth.application.dto;

public record SignInDto(
        String name,
        String email,
        String oauthId,
        String oauthType
) {
}
