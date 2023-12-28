package com.baro.oauth.infra.kakao.config;

import com.baro.oauth.infra.google.config.GoogleOAuthProperty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class GoogleOAuthPropertyTest {

    @Autowired
    private GoogleOAuthProperty googleOAuthProperty;

    @Test
    void Google_프로퍼티_값_바인딩_테스트() {
        assertNotNull(googleOAuthProperty.clientId());
    }
}
