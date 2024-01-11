package com.baro.auth.fake.jwt;

import com.baro.auth.application.TokenStorage;
import com.baro.auth.exception.AuthException;
import com.baro.auth.exception.AuthExceptionType;
import com.baro.auth.exception.jwt.JwtTokenException;
import com.baro.auth.exception.jwt.JwtTokenExceptionType;
import com.baro.common.time.TimeServer;
import com.baro.common.time.fake.FakeTimeServer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FakeTokenStorage implements TokenStorage {

    private final Map<String, TokenInfo> tokens = new ConcurrentHashMap<>();
    private final long ttl;
    private TimeServer timeServer = new FakeTimeServer(Instant.now());

    public FakeTokenStorage(long ttl) {
        this.ttl = ttl;
    }

    @Override
    public void saveRefreshToken(String key, String value) {
        tokens.put("RT:" + key, new TokenInfo(value, timeServer.now().plus(ttl, ChronoUnit.MILLIS)));
    }

    @Override
    public String findRefreshToken(String key) {
        if (tokens.get("RT:" + key).expireTime().isAfter(timeServer.now())){
            return tokens.get("RT:" + key).token();
        }
        if (!tokens.containsKey("RT:" + key)) {
            throw new AuthException(AuthExceptionType.REFRESH_TOKEN_DOES_NOT_EXIST);
        }
        throw new JwtTokenException(JwtTokenExceptionType.EXPIRED_JWT_TOKEN);
    }

    record TokenInfo(String token, Instant expireTime) {
    }
}
