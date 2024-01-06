package com.baro.auth.infra.jwt;

import com.baro.auth.domain.Token;
import com.baro.member.domain.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
class JwtTokenCreator {

    private final JwtProperty jwtProperty;

    Token createToken(Member member, Instant now) {
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtProperty.secretKey().getBytes());
        Instant accessTokenExpiresIn = now.plusSeconds(jwtProperty.accessTokenExpireTime());
        Instant refreshTokenExpiresIn = now.plusSeconds(jwtProperty.refreshTokenExpireTime());

        String accessToken = Jwts.builder()
                .claim("id", member.getId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(accessTokenExpiresIn))
                .signWith(secretKey)
                .compact();
        String refreshToken = Jwts.builder()
                .expiration(Date.from(refreshTokenExpiresIn))
                .signWith(secretKey)
                .compact();

        return new Token(accessToken, refreshToken);
    }
}
