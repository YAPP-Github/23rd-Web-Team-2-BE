package com.baro.auth.infra.oauth.google.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class GoogleOAuthPropertyTest {

    @Autowired
    private GoogleOAuthProperty googleOAuthProperty;

    @Test
    void Google_프로퍼티_값_바인딩_테스트() {
        assertThat(googleOAuthProperty)
                .usingRecursiveAssertion()
                .allFieldsSatisfy(Objects::nonNull);
    }
}
