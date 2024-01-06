package com.baro.auth.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class TokenTest {

    @Test
    void token_동일성_테스트() {
        assertThat(new Token("accessToken", "refreshToken"))
                .isEqualTo(new Token("accessToken", "refreshToken"));
    }
}
