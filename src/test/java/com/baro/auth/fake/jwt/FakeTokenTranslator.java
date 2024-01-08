package com.baro.auth.fake.jwt;

import com.baro.auth.application.TokenTranslator;
import com.baro.auth.domain.Token;

public class FakeTokenTranslator implements TokenTranslator {

    @Override
    public Token encode(Long id, String ipAddress) {
        return new Token("accessToken", "refreshToken");
    }

    @Override
    public Long decodeAccessToken(String token) {
        return 1L;
    }

    @Override
    public String decodeRefreshToken(String token) {
        return "127.0.0.1";
    }
}
