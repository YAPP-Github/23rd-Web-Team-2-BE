package com.baro.auth.infra.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class JwtPropertyTest {

    @Autowired
    private JwtProperty jwtProperty;

    @Test
    void Jwt_프로퍼티_값_바인딩_테스트() {
        // when & then
        assertThat(jwtProperty)
                .usingRecursiveAssertion()
                .allFieldsSatisfy(Objects::nonNull);
    }
}
