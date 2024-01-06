package com.baro.auth.application.oauth;

import static org.assertj.core.api.Assertions.assertThat;

import com.baro.auth.application.oauth.dto.OAuthMemberInfo;
import com.baro.auth.application.oauth.dto.OAuthTokenInfo;
import com.baro.auth.domain.oauth.OAuthServiceType;
import com.baro.auth.fake.oauth.FakeGoogleOAuthClient;
import com.baro.auth.fake.oauth.FakeKakaoOAuthClient;
import com.baro.auth.fake.oauth.FakeNaverOAuthClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

@SuppressWarnings("NonAsciiCharacters")
class OAuthInfoProviderTest {

    private OAuthInfoProvider oAuthInfoProvider;
    OAuthClient fakeKakaoOAuthClient = new FakeKakaoOAuthClient();
    OAuthClient fakeNaverOAuthClient = new FakeNaverOAuthClient();
    OAuthClient fakeGoogleOAuthClient = new FakeGoogleOAuthClient();

    @BeforeEach
    void setUp() {
        oAuthInfoProvider = new OAuthInfoProvider(Set.of(fakeKakaoOAuthClient, fakeNaverOAuthClient, fakeGoogleOAuthClient));
    }

    @Test
    void 카카오의_SignInUrl을_가져온다() {
        // given
        String oAuthService = OAuthServiceType.KAKAO.name();

        // when
        String signInUrl = oAuthInfoProvider.getSignInUrl(oAuthService);

        // then
        assertThat(signInUrl).isEqualTo(fakeKakaoOAuthClient.getSignInUrl());
    }

    @Test
    void 네이버의_SignInUrl을_가져온다() {
        // given
        String oAuthService = OAuthServiceType.NAVER.name();

        // when
        String signInUrl = oAuthInfoProvider.getSignInUrl(oAuthService);

        // then
        assertThat(signInUrl).isEqualTo(fakeNaverOAuthClient.getSignInUrl());
    }

    @Test
    void 구글의_SignInUrl을_가져온다() {
        // given
        String oAuthService = OAuthServiceType.GOOGLE.name();

        // when
        String signInUrl = oAuthInfoProvider.getSignInUrl(oAuthService);

        // then
        assertThat(signInUrl).isEqualTo(fakeGoogleOAuthClient.getSignInUrl());
    }

    @Test
    void 카카오의_멤버_정보를_가져온다() {
        // given
        String oAuthService = OAuthServiceType.KAKAO.name();
        OAuthTokenInfo oAuthTokenInfo = fakeKakaoOAuthClient.requestAccessToken("code");

        // when
        OAuthMemberInfo memberInfo = oAuthInfoProvider.getMemberInfo(oAuthService, oAuthTokenInfo.accessToken());

        // then
        assertThat(memberInfo).isEqualTo(fakeKakaoOAuthClient.requestMemberInfo(oAuthTokenInfo));
    }

    @Test
    void 네이버의_멤버_정보를_가져온다() {
        // given
        String oAuthService = OAuthServiceType.NAVER.name();
        OAuthTokenInfo oAuthTokenInfo = fakeNaverOAuthClient.requestAccessToken("code");

        // when
        OAuthMemberInfo memberInfo = oAuthInfoProvider.getMemberInfo(oAuthService, oAuthTokenInfo.accessToken());

        // then
        assertThat(memberInfo).isEqualTo(fakeNaverOAuthClient.requestMemberInfo(oAuthTokenInfo));
    }

    @Test
    void 구글의_멤버_정보를_가져온다() {
        // given
        String oAuthService = OAuthServiceType.GOOGLE.name();
        OAuthTokenInfo oAuthTokenInfo = fakeGoogleOAuthClient.requestAccessToken("code");

        // when
        OAuthMemberInfo memberInfo = oAuthInfoProvider.getMemberInfo(oAuthService, oAuthTokenInfo.accessToken());

        // then
        assertThat(memberInfo).isEqualTo(fakeGoogleOAuthClient.requestMemberInfo(oAuthTokenInfo));
    }
}
