package com.baro.auth.fake.jwt;

import com.baro.auth.application.TokenTranslator;
import com.baro.auth.domain.Token;

public class FakeTokenTranslator implements TokenTranslator {

    @Override
    public Token encode(Long id) {
        return new Token("accessToken", "refreshToken");
    }

    @Override
    public Long decodeAccessToken(String token) {
        return 1L;
    }

    @Override
    public void decodeRefreshToken(String token) {
    }
}
