package com.baro.auth.infra.oauth.google.dto;

import com.baro.auth.application.oauth.dto.OAuthMemberInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleMemberResponse(
        String id,
        String email,
        String name
) {

    public OAuthMemberInfo toOAuthMemberInfo() {
        return new OAuthMemberInfo(id, name, email);
    }
}
