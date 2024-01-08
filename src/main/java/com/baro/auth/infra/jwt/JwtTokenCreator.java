package com.baro.auth.infra.jwt;

import com.baro.auth.application.TokenCreator;
import com.baro.auth.domain.Token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
class JwtTokenCreator implements TokenCreator {

    private final JwtProperty jwtProperty;

    public Token createToken(Long id, Instant now) {
        SecretKey accessTokenSecretKey = Keys.hmacShaKeyFor(jwtProperty.accessSecretKey().getBytes());
        SecretKey refreshTokenSecretKey = Keys.hmacShaKeyFor(jwtProperty.refreshSecretKey().getBytes());
        Instant accessTokenExpiresIn = now.plusSeconds(jwtProperty.accessTokenExpireTime());
        Instant refreshTokenExpiresIn = now.plusSeconds(jwtProperty.refreshTokenExpireTime());

        String accessToken = Jwts.builder()
                .claim("id", id)
                .issuedAt(Date.from(now))
                .expiration(Date.from(accessTokenExpiresIn))
                .signWith(accessTokenSecretKey)
                .compact();
        String refreshToken = Jwts.builder()
                .expiration(Date.from(refreshTokenExpiresIn))
                .signWith(refreshTokenSecretKey)
                .compact();

        return new Token(accessToken, refreshToken);
    }
}
