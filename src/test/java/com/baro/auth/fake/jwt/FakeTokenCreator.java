package com.baro.auth.fake.jwt;

import com.baro.auth.domain.Token;
import com.baro.auth.application.TokenCreator;

import java.time.Instant;

public class FakeTokenCreator implements TokenCreator {

    private String accessToken;
    private String refreshToken;

    public FakeTokenCreator(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public Token createToken(Long id, Instant now) {
        return new Token(accessToken, refreshToken);
    }
}
