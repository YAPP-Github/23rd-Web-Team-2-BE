package com.baro.auth.infra.jwt;

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

    public Long decrypt(String authHeader) {
        String token = validateTokenType(authHeader);
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

    private String validateTokenType(String authHeader) {
        if(!authHeader.isBlank() && authHeader.startsWith(jwtProperty.bearerType())) {
            return authHeader.substring(jwtProperty.bearerType().length() + 1);
        }
        throw new JwtTokenException(JwtTokenExceptionType.NOT_BEARER_TYPE);
    }
}
