package com.baro.oauth.infra.google;

import com.baro.oauth.application.OAuthClient;
import com.baro.oauth.application.dto.OAuthMemberInfo;
import com.baro.oauth.application.dto.OAuthTokenInfo;
import com.baro.oauth.domain.OAuthServiceType;
import com.baro.oauth.infra.google.config.GoogleOAuthProperty;
import com.baro.oauth.infra.google.dto.GoogleMemberResponse;
import com.baro.oauth.infra.google.dto.GoogleTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class GoogleOAuthClient implements OAuthClient {

    private final GoogleOAuthProperty googleOAuthProperty;
    private final GoogleRequestApi googleRequestApi;

    @Override
    public String getSignInUrl() {
        return UriComponentsBuilder
                .fromUriString(googleOAuthProperty.signInAuthorizeUrl())
                .queryParam("client_id", googleOAuthProperty.clientId())
                .queryParam("redirect_uri", googleOAuthProperty.redirectUri())
                .queryParam("response_type", googleOAuthProperty.responseType())
                .queryParam("scope", String.join(",", googleOAuthProperty.scope()))
                .toUriString();
    }

    @Override
    public OAuthServiceType getOAuthService() {
        return OAuthServiceType.GOOGLE;
    }

    @Override
    public OAuthTokenInfo requestAccessToken(String code) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("client_id", googleOAuthProperty.clientId());
        params.put("redirect_uri", googleOAuthProperty.redirectUri());
        params.put("code", code);
        params.put("client_secret", googleOAuthProperty.clientSecret());
        GoogleTokenResponse googleTokenResponse = googleRequestApi.requestToken(params);
        return googleTokenResponse.toOAuthTokenInfo();
    }

    @Override
    public OAuthMemberInfo requestMemberInfo(OAuthTokenInfo oAuthTokenInfo) {
        String authorization = "Bearer " + oAuthTokenInfo.accessToken();
        GoogleMemberResponse response = googleRequestApi.requestMemberInfo(authorization);
        return response.toOAuthMemberInfo();
    }
}
