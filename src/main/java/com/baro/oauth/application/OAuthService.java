package com.baro.oauth.application;

import com.baro.oauth.application.dto.OAuthMemberInfo;
import com.baro.oauth.application.dto.OAuthTokenInfo;
import com.baro.oauth.domain.OAuthServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class OAuthService {

    private final OAuthClientComponents clientComponents;

    @Transactional(readOnly = true)
    public String getSignInUrl(String oAuthService) {
        OAuthClient client = getClientByOAuthService(oAuthService);
        return client.getSignInUrl();
    }

    public void signIn(String oAuthService, String authCode) {
        OAuthClient client = getClientByOAuthService(oAuthService);
        OAuthTokenInfo oAuthTokenInfo = client.requestAccessToken(authCode);
        OAuthMemberInfo oAuthMemberInfo = client.requestMemberInfo(oAuthTokenInfo);
        //TODO: 인증된 OAuth 정보로 회원가입 또는 로그인 처리
    }

    private OAuthClient getClientByOAuthService(final String oAuthService) {
        OAuthServiceType oAuthServiceType = OAuthServiceType.from(oAuthService);
        return clientComponents.getClient(oAuthServiceType);
    }
}
