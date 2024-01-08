package com.baro.auth.infra.oauth.naver;

import com.baro.auth.application.oauth.OAuthClient;
import com.baro.auth.application.oauth.dto.OAuthMemberInfo;
import com.baro.auth.application.oauth.dto.OAuthTokenInfo;
import com.baro.auth.domain.oauth.OAuthServiceType;
import com.baro.auth.infra.oauth.naver.config.NaverOAuthProperty;
import com.baro.auth.infra.oauth.naver.dto.NaverMemberResponse;
import com.baro.auth.infra.oauth.naver.dto.NaverTokenResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

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
                .queryParam("redirect_uri", naverOAuthProperty.redirectUri())
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
        NaverMemberResponse naverMemberResponse = naverRequestApi.requestMemberInfo(
                "Bearer " + oAuthTokenInfo.accessToken());
        return naverMemberResponse.toOAuthMemberInfo();
    }
}
