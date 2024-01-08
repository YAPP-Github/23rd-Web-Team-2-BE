package com.baro.auth.infra.oauth.kakao.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class KakaoOAuthPropertyTest {

    @Autowired
    private KakaoOAuthProperty kakaoOAuthProperty;

    @Test
    void Kakao_프로퍼티_값_바인딩_테스트() {
        // when
        String[] userInfoRequestScopes = kakaoOAuthProperty.scope();

        // then
        assertThat(userInfoRequestScopes).contains("account_email");
    }
}
