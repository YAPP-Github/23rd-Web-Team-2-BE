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
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class OAuthInfoProviderTest {

    @Autowired
    private OAuthInfoProvider oAuthInfoProvider;

    @BeforeEach
    void setUp() {
        OAuthClient fakeKakaoOAuthClient = new FakeKakaoOAuthClient();
        OAuthClient fakeNaverOAuthClient = new FakeNaverOAuthClient();
        OAuthClient fakeGoogleOAuthClient = new FakeGoogleOAuthClient();
        oAuthInfoProvider = new OAuthInfoProvider(Set.of(fakeKakaoOAuthClient, fakeNaverOAuthClient, fakeGoogleOAuthClient));
    }

//    @Test
//    void Client_조회_테스트() {
//        // given
//        OAuthServiceType oAuthServiceType = OAuthServiceType.KAKAO;
//
//        // when
//        OAuthClient client = clientComponents.getClient(oAuthServiceType);
//
//        // then
//        assertThat(client).isInstanceOf(KakaoOAuthClient.class);
//    }
}
