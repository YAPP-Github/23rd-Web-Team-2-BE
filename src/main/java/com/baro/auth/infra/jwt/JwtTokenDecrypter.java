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
    public String decryptRefreshToken(String refreshToken) {
        SecretKey refreshTokenSecretKey = Keys.hmacShaKeyFor(jwtProperty.refreshSecretKey().getBytes());
        try {
            String ipAddress = Jwts.parser().verifyWith(refreshTokenSecretKey)
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload()
                    .get("ip", String.class);
            return ipAddress;
        } catch (ExpiredJwtException e) {
            throw new JwtTokenException(JwtTokenExceptionType.EXPIRED_JWT_TOKEN);
        } catch (JwtException e) {
            throw new JwtTokenException(JwtTokenExceptionType.INVALID_JWT_TOKEN);
        }
    }

    private String validateTokenType(String authorization) {
        if(authorization == null) throw new JwtTokenException(JwtTokenExceptionType.AUTHORIZATION_NULL);
        if (authorization.startsWith(jwtProperty.bearerType())) {
            return authorization.substring(jwtProperty.bearerType().length() + 1);
        }
        throw new JwtTokenException(JwtTokenExceptionType.NOT_BEARER_SCHEME);
    }
}
