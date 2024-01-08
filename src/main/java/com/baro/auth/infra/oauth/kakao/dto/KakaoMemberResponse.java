package com.baro.auth.infra.oauth.kakao.dto;

import com.baro.auth.application.oauth.dto.OAuthMemberInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = SnakeCaseStrategy.class)
public record KakaoMemberResponse(
        Long id,
        String connectedAt,
        Properties properties,
        KakaoAccount kakaoAccount
) {

    public OAuthMemberInfo toOAuthMemberInfo() {
        return new OAuthMemberInfo(
                String.valueOf(id),
                properties.nickname,
                "email" //TODO: 비즈앱 전환후 email 추가
        );
    }

    public record Properties(
            String nickname
    ) {
    }

    record KakaoAccount(
            Boolean profileNicknameNeedsAgreement,
            Profile profile
    ) {
        record Profile(
                String nickname
        ) {
        }
    }
}
