package com.baro.oauth.application.dto;

public record OAuthMemberInfo(
        String oAuthId,
        String nickname
        // TODO: User 설계에 따라 필드 추가
) {
}
