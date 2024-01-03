package com.baro.auth.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
class TokenTest {

    @Test
    void token_동일성_테스트() {
        assertEquals(new Token("accessToken", "refreshToken"),
                new Token("accessToken", "refreshToken"));
    }
}
