package com.baro.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TokenTest {

    @Test
    void token_동일성_테스트() {
        Token actual = new Token("accessToken", "refreshToken");
        Token expected = new Token("accessToken", "refreshToken");
        assertThat(actual).isEqualTo(expected);
    }
}
