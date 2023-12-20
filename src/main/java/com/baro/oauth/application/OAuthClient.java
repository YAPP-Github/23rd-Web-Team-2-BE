package com.baro.oauth.application;

import com.baro.oauth.domain.OAuthServiceType;

public interface OAuthClient {

    String getSignInUrl();

    OAuthServiceType getOAuthService();
}
