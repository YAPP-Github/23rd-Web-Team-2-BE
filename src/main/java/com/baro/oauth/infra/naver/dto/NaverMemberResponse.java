package com.baro.oauth.infra.naver.dto;

import com.baro.oauth.application.dto.OAuthMemberInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NaverMemberResponse(
        String resultcode,
        String message,
        Response response
) {

    public OAuthMemberInfo toOAuthMemberInfo() {
        return new OAuthMemberInfo(
                String.valueOf(response.id),
                response.name,
                response.email
        );
    }

    public record Response(
            String id,
            String name,
            String email
    ){
    }
}
