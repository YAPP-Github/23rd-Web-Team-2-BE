package com.baro.auth.infra.jwt;

import com.baro.auth.application.TokenDecrypter;
import com.baro.auth.exception.jwt.JwtTokenException;
import com.baro.auth.exception.jwt.JwtTokenExceptionType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@RequiredArgsConstructor
@Component
class JwtTokenDecrypter implements TokenDecrypter {

    private final JwtProperty jwtProperty;

    @Override
    public Long decryptAccessToken(String authorization) {
        String token = validateTokenType(authorization);
        SecretKey accessTokenSecretKey = Keys.hmacShaKeyFor(jwtProperty.accessSecretKey().getBytes());
        try {
            Long id = Jwts.parser().verifyWith(accessTokenSecretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("id", Long.class);
            return id;
        } catch (ExpiredJwtException e) {
            throw new JwtTokenException(JwtTokenExceptionType.EXPIRED_JWT_TOKEN);
        } catch (JwtException e) {
            throw new JwtTokenException(JwtTokenExceptionType.INVALID_JWT_TOKEN);
        }
    }

    @Override
    public void decryptRefreshToken(String token) {
        String refreshToken = validateTokenType(token);
        SecretKey refreshTokenSecretKey = Keys.hmacShaKeyFor(jwtProperty.refreshSecretKey().getBytes());
        try {
            Jwts.parser().verifyWith(refreshTokenSecretKey)
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new JwtTokenException(JwtTokenExceptionType.EXPIRED_JWT_TOKEN);
        } catch (JwtException e) {
            throw new JwtTokenException(JwtTokenExceptionType.INVALID_JWT_TOKEN);
        }
    }

    private String validateTokenType(String authorization) {
        if (authorization == null) {
            throw new JwtTokenException(JwtTokenExceptionType.AUTHORIZATION_NULL);
        }
        if (authorization.startsWith(jwtProperty.scheme())) {
            return authorization.substring(jwtProperty.scheme().length() + 1);
        }
        throw new JwtTokenException(JwtTokenExceptionType.NOT_BEARER_SCHEME);
    }
}
