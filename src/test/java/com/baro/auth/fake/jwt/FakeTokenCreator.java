package com.baro.auth.fake.jwt;

import com.baro.auth.application.TokenCreator;
import com.baro.auth.domain.Token;
import java.time.Instant;

public class FakeTokenCreator implements TokenCreator {

    private final String accessToken;
    private final String refreshToken;

    public FakeTokenCreator(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public Token createToken(Long id, Instant now) {
        return new Token(accessToken, refreshToken);
    }
}
