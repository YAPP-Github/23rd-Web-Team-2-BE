package com.baro.auth.fake.oauth;

import com.baro.auth.application.oauth.OAuthClient;
import com.baro.auth.application.oauth.dto.OAuthMemberInfo;
import com.baro.auth.application.oauth.dto.OAuthTokenInfo;
import com.baro.auth.domain.oauth.OAuthServiceType;

public class FakeGoogleOAuthClient implements OAuthClient {

    @Override
    public String getSignInUrl(String host) {
        return null;
    }

    @Override
    public OAuthServiceType getOAuthService() {
        return OAuthServiceType.GOOGLE;
    }

    @Override
    public OAuthTokenInfo requestAccessToken(String host, String code) {
        return new OAuthTokenInfo("accessToken", "refreshToken", 1000);
    }

    @Override
    public OAuthMemberInfo requestMemberInfo(OAuthTokenInfo oAuthTokenInfo) {
        return new OAuthMemberInfo("googleId", "googleName", "googleEmail");
    }
}
