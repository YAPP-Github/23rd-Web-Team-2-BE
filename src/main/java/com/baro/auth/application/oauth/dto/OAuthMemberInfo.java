package com.baro.auth.application.oauth.dto;

public record OAuthMemberInfo(
        String oAuthId,
        String name,
        String email
        // TODO: User 설계에 따라 필드 추가
) {
}
