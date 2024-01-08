package com.baro.auth.fake.jwt;

import com.baro.auth.application.TokenStorage;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FakeTokenStorage implements TokenStorage {

    private final Map<String, TokenInfo> tokens = new ConcurrentHashMap<>();

    @Override
    public void saveRefreshToken(String key, String value) {
        tokens.put("RT:" + key, new TokenInfo(value, LocalDateTime.now().plusDays(1)));
    }

    @Override
    public String findRefreshToken(String key) {
        if(tokens.get("RT:" + key).expireTime().isAfter(LocalDateTime.now()))
            return tokens.get("RT:" + key).token();
        return null;
    }

    record TokenInfo(String token, LocalDateTime expireTime) {}
}
