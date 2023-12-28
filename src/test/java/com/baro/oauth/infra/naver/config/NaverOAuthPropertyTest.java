package com.baro.oauth.infra.naver.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class NaverOAuthPropertyTest {

    @Autowired
    private NaverOAuthProperty property;

    @Test
    void Naver_프로퍼티값_바인딩_테스트() {
        assertNotNull(property.clientId());
    }
}
