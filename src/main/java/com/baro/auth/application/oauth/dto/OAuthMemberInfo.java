package com.baro.auth.application.oauth.dto;

public record OAuthMemberInfo(
        String oAuthId,
        String name,
        String email
) {
}
