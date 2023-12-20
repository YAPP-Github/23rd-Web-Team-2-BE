package com.baro.oauth.domain;

import java.util.Arrays;

public enum OAuthServiceType {

    KAKAO,
    ;

    public static OAuthServiceType from(String oAuthServiceType) {
        return Arrays.stream(OAuthServiceType.values())
                .filter(type -> type.name().equalsIgnoreCase(oAuthServiceType))
                .findFirst()
                .orElseThrow(); //TODO 예외 처리
    }
}
