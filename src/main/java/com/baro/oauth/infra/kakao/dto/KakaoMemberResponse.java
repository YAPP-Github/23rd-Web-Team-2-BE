package com.baro.oauth.infra.kakao.dto;

import com.baro.oauth.application.dto.OAuthMemberInfo;
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

    record Properties(
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
