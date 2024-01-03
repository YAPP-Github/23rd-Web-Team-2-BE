package com.baro.auth.application.oauth;

import static org.assertj.core.api.Assertions.assertThat;

import com.baro.auth.domain.oauth.OAuthServiceType;
import com.baro.auth.fake.oauth.FakeGoogleOAuthClient;
import com.baro.auth.fake.oauth.FakeKakaoOAuthClient;
import com.baro.auth.fake.oauth.FakeNaverOAuthClient;
import com.baro.auth.infra.oauth.kakao.KakaoOAuthClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.Set;

@SuppressWarnings("NonAsciiCharacters")
class OAuthInfoProviderTest {

    private OAuthInfoProvider oAuthInfoProvider;

    @BeforeEach
    void setUp() {
        OAuthClient fakeKakaoOAuthClient = new FakeKakaoOAuthClient();
        OAuthClient fakeNaverOAuthClient = new FakeNaverOAuthClient();
        OAuthClient fakeGoogleOAuthClient = new FakeGoogleOAuthClient();
        oAuthInfoProvider = new OAuthInfoProvider(Set.of(fakeKakaoOAuthClient, fakeNaverOAuthClient, fakeGoogleOAuthClient));
    }

//    @Test
//    void 카카오의_SignInUrl을_가져온다() {
//        // given
//        String oAuthService = OAuthServiceType.KAKAO.name();
//
//        // when
//        String signInUrl = oAuthInfoProvider.getSignInUrl(oAuthService);
//
//        // then
//        assertThat(signInUrl).isEqualTo(KakaoOAuthClient.SIGN_IN_URL);
//    }
}
