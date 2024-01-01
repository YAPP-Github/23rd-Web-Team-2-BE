package com.baro.auth.domain.oauth;

import com.baro.auth.exception.oauth.OAuthException;
import com.baro.auth.exception.oauth.OAuthExceptionType;
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
