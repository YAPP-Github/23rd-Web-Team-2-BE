package com.baro.oauth.application;

import com.baro.oauth.application.dto.OAuthMemberInfo;
import com.baro.oauth.application.dto.OAuthTokenInfo;
import com.baro.oauth.domain.OAuthServiceType;

public interface OAuthClient {

    String getSignInUrl();

    OAuthServiceType getOAuthService();

    OAuthTokenInfo requestAccessToken(String code);

    OAuthMemberInfo requestMemberInfo(OAuthTokenInfo oAuthTokenInfo);
}
