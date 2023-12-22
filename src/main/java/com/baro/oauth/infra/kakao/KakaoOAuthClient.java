package com.baro.oauth.infra.kakao;

import com.baro.oauth.application.OAuthClient;
import com.baro.oauth.application.dto.OAuthMemberInfo;
import com.baro.oauth.application.dto.OAuthTokenInfo;
import com.baro.oauth.domain.OAuthServiceType;
import com.baro.oauth.infra.kakao.config.KakaoOAuthProperty;
import com.baro.oauth.infra.kakao.dto.KakaoMemberResponse;
import com.baro.oauth.infra.kakao.dto.KakaoTokenResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class KakaoOAuthClient implements OAuthClient {

    private final KakaoOAuthProperty kakaoOAuthProperty;
    private final KakaoRequestApi kakaoRequestApi;

    @Override
    public String getSignInUrl() {
        return UriComponentsBuilder
                .fromUriString(kakaoOAuthProperty.signInAuthorizeUrl())
                .queryParam("response_type", kakaoOAuthProperty.responseType())
                .queryParam("client_id", kakaoOAuthProperty.clientId())
                .queryParam("redirect_uri", kakaoOAuthProperty.redirectUrl())
                .queryParam("scope", String.join(",", kakaoOAuthProperty.scope()))
                .toUriString();
    }

    @Override
    public OAuthTokenInfo requestAccessToken(String authCode) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("client_id", kakaoOAuthProperty.clientId());
        params.put("redirect_uri", kakaoOAuthProperty.redirectUrl());
        params.put("code", authCode);
        params.put("client_secret", kakaoOAuthProperty.clientSecret());
        KakaoTokenResponse kakaoTokenResponse = kakaoRequestApi.requestToken(params);
        return kakaoTokenResponse.toOAuthTokenInfo();
    }

    @Override
    public OAuthMemberInfo requestMemberInfo(OAuthTokenInfo oAuthTokenInfo) {
        String authorization = "Bearer " + oAuthTokenInfo.accessToken();
        KakaoMemberResponse response = kakaoRequestApi.requestMemberInfo(authorization);
        return response.toOAuthMemberInfo();
    }

    @Override
    public OAuthServiceType getOAuthService() {
        return OAuthServiceType.KAKAO;
    }
}
