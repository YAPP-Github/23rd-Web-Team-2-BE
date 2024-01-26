package com.baro.auth.application.oauth;

import static org.assertj.core.api.Assertions.assertThat;

import com.baro.auth.application.oauth.dto.OAuthMemberInfo;
import com.baro.auth.application.oauth.dto.OAuthTokenInfo;
import com.baro.auth.domain.oauth.OAuthServiceType;
import com.baro.auth.fake.oauth.FakeGoogleOAuthClient;
import com.baro.auth.fake.oauth.FakeKakaoOAuthClient;
import com.baro.auth.fake.oauth.FakeNaverOAuthClient;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OAuthInfoProviderTest {

    final OAuthClient fakeKakaoOAuthClient = new FakeKakaoOAuthClient();
    final OAuthClient fakeNaverOAuthClient = new FakeNaverOAuthClient();
    final OAuthClient fakeGoogleOAuthClient = new FakeGoogleOAuthClient();
    private OAuthInfoProvider oAuthInfoProvider;

    @BeforeEach
    void setUp() {
        oAuthInfoProvider =
                new OAuthInfoProvider(Set.of(fakeKakaoOAuthClient, fakeNaverOAuthClient, fakeGoogleOAuthClient));
    }

    @Test
    void 카카오의_SignInUrl을_가져온다() {
        // given
        String oAuthService = OAuthServiceType.KAKAO.name();
        String host = "http://localhost:3000";

        // when
        String signInUrl = oAuthInfoProvider.getSignInUrl(oAuthService, host);

        // then
        assertThat(signInUrl).isEqualTo(fakeKakaoOAuthClient.getSignInUrl(host));
    }

    @Test
    void 네이버의_SignInUrl을_가져온다() {
        // given
        String oAuthService = OAuthServiceType.NAVER.name();
        String host = "http://localhost:3000";

        // when
        String signInUrl = oAuthInfoProvider.getSignInUrl(oAuthService, host);

        // then
        assertThat(signInUrl).isEqualTo(fakeNaverOAuthClient.getSignInUrl(host));
    }

    @Test
    void 구글의_SignInUrl을_가져온다() {
        // given
        String oAuthService = OAuthServiceType.GOOGLE.name();
        String host = "http://localhost:3000";

        // when
        String signInUrl = oAuthInfoProvider.getSignInUrl(oAuthService, host);

        // then
        assertThat(signInUrl).isEqualTo(fakeGoogleOAuthClient.getSignInUrl(host));
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
