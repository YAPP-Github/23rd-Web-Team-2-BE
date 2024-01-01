package com.baro.auth.infra.oauth.kakao.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.baro.auth.infra.oauth.kakao.config.KakaoOAuthProperty;
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
        assertThat(kakaoOAuthProperty.clientId()).isNotNull();
    }
}
