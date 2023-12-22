package com.baro.oauth.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.baro.oauth.domain.OAuthServiceType;
import com.baro.oauth.infra.kakao.KakaoOAuthClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class OAuthClientComponentsTest {

    @Autowired
    private OAuthClientComponents clientComponents;

    @Test
    void Client_조회_테스트() {
        // given
        OAuthServiceType oAuthServiceType = OAuthServiceType.KAKAO;

        // when
        OAuthClient client = clientComponents.getClient(oAuthServiceType);

        // then
        assertThat(client).isInstanceOf(KakaoOAuthClient.class);
    }
}
