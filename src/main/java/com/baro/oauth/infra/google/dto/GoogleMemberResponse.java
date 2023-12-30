package com.baro.oauth.infra.google.dto;

import com.baro.oauth.application.dto.OAuthMemberInfo;
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
