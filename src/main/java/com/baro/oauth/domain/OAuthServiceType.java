package com.baro.oauth.domain;

import com.baro.oauth.exception.OAuthException;
import com.baro.oauth.exception.OAuthExceptionType;
import java.util.Arrays;

public enum OAuthServiceType {

    KAKAO,
    GOOGLE,
    NAVER,
    ;

    public static OAuthServiceType from(String oAuthServiceType) {
        return Arrays.stream(OAuthServiceType.values())
                .filter(type -> type.name().equalsIgnoreCase(oAuthServiceType))
                .findFirst()
                .orElseThrow(() -> new OAuthException(OAuthExceptionType.NOT_SUPPORTED_OAUTH_TYPE));
    }
}
