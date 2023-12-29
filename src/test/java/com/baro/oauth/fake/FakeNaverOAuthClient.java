package com.baro.oauth.fake;

import com.baro.oauth.application.OAuthClient;
import com.baro.oauth.application.dto.OAuthMemberInfo;
import com.baro.oauth.application.dto.OAuthTokenInfo;
import com.baro.oauth.domain.OAuthServiceType;

public class FakeNaverOAuthClient implements OAuthClient {
    @Override
    public String getSignInUrl() {
        return null;
    }

    @Override
    public OAuthServiceType getOAuthService() {
        return OAuthServiceType.NAVER;
    }

    @Override
    public OAuthTokenInfo requestAccessToken(String code) {
        return new OAuthTokenInfo("accessToken", "refreshToken", 1000);
    }

    @Override
    public OAuthMemberInfo requestMemberInfo(OAuthTokenInfo oAuthTokenInfo) {
        return new OAuthMemberInfo("naverId", "naverName", "naverEmail");
    }
}
