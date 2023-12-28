package com.baro.oauth.infra.naver;

import com.baro.oauth.application.OAuthClient;
import com.baro.oauth.application.dto.OAuthMemberInfo;
import com.baro.oauth.application.dto.OAuthTokenInfo;
import com.baro.oauth.domain.OAuthServiceType;
import com.baro.oauth.infra.naver.config.NaverOAuthProperty;
import com.baro.oauth.infra.naver.dto.NaverMemberResponse;
import com.baro.oauth.infra.naver.dto.NaverTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class NaverOAuthClient implements OAuthClient {

    private final NaverOAuthProperty naverOAuthProperty;
    private final NaverRequestApi naverRequestApi;

    @Override
    public String getSignInUrl() {
        return UriComponentsBuilder
                .fromUriString(naverOAuthProperty.signInAuthorizeUrl())
                .queryParam("response_type", naverOAuthProperty.responseType())
                .queryParam("client_id", naverOAuthProperty.clientId())
                .queryParam("redirect_uri", naverOAuthProperty.redirectUrl())
                .queryParam("state", naverOAuthProperty.state())
                .toUriString();
    }

    @Override
    public OAuthServiceType getOAuthService() {
        return OAuthServiceType.NAVER;
    }

    @Override
    public OAuthTokenInfo requestAccessToken(String code) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("grant_type", naverOAuthProperty.grantType());
        params.put("client_id", naverOAuthProperty.clientId());
        params.put("client_secret", naverOAuthProperty.clientSecret());
        params.put("code", code);
        params.put("state", naverOAuthProperty.state());
        NaverTokenResponse naverTokenResponse = naverRequestApi.requestToken(params);
        return naverTokenResponse.toOAuthTokenInfo();
    }

    @Override
    public OAuthMemberInfo requestMemberInfo(OAuthTokenInfo oAuthTokenInfo) {
        NaverMemberResponse naverMemberResponse = naverRequestApi.requestMemberInfo("Bearer " + oAuthTokenInfo.accessToken());
        return naverMemberResponse.toOAuthMemberInfo();
    }
}
