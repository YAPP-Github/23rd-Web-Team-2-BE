package com.baro.oauth.infra.kakao;

import com.baro.oauth.application.OAuthClient;
import com.baro.oauth.infra.kakao.entity.KakaoOAuthProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class KakaoOAuthClient implements OAuthClient {

    private final KakaoOAuthProperty kakaoOAuthProperty;

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
}
