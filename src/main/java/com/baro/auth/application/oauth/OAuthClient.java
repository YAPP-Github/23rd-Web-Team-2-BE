package com.baro.auth.application.oauth;

import com.baro.auth.application.oauth.dto.OAuthMemberInfo;
import com.baro.auth.application.oauth.dto.OAuthTokenInfo;
import com.baro.auth.domain.oauth.OAuthServiceType;

public interface OAuthClient {

    String getSignInUrl(String host);

    OAuthServiceType getOAuthService();

    OAuthTokenInfo requestAccessToken(String code);

    OAuthMemberInfo requestMemberInfo(OAuthTokenInfo oAuthTokenInfo);
}
