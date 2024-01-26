package com.baro.auth.infra.oauth.google;

import com.baro.auth.application.oauth.OAuthClient;
import com.baro.auth.application.oauth.dto.OAuthMemberInfo;
import com.baro.auth.application.oauth.dto.OAuthTokenInfo;
import com.baro.auth.domain.oauth.OAuthServiceType;
import com.baro.auth.infra.oauth.google.config.GoogleOAuthProperty;
import com.baro.auth.infra.oauth.google.dto.GoogleMemberResponse;
import com.baro.auth.infra.oauth.google.dto.GoogleTokenResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class GoogleOAuthClient implements OAuthClient {

    private final GoogleOAuthProperty googleOAuthProperty;
    private final GoogleRequestApi googleRequestApi;

    @Override
    public String getSignInUrl(String host) {
        return UriComponentsBuilder
                .fromUriString(googleOAuthProperty.signInAuthorizeUrl())
                .queryParam("client_id", googleOAuthProperty.clientId())
                .queryParam("response_type", googleOAuthProperty.responseType())
                .queryParam("redirect_uri", host + googleOAuthProperty.redirectUri())
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
