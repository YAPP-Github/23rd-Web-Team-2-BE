package com.baro.auth.infra.jwt;

import com.baro.auth.application.TokenStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedisTokenStorage implements TokenStorage {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProperty jwtProperty;

    public void saveRefreshToken(String key, String value) {
        redisTemplate.opsForValue().set("RT:" + key, jwtProperty.scheme() + " " + value,
                jwtProperty.refreshTokenExpireTime(), TimeUnit.MILLISECONDS);
    }

    public String findRefreshToken(String key) {
        return redisTemplate.opsForValue().get("RT:" + key);
    }
}
