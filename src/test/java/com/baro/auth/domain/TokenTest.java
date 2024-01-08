package com.baro.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class TokenTest {

    @Test
    void token_동일성_테스트() {
        Token actual = new Token("accessToken", "refreshToken");
        Token expected = new Token("accessToken", "refreshToken");
        assertThat(actual).isEqualTo(expected);
    }
}
