package com.baro.auth.infra.jwt;

import com.baro.auth.exception.jwt.JwtTokenException;
import com.baro.auth.exception.jwt.JwtTokenExceptionType;
import com.baro.common.auth.AuthMember;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@RequiredArgsConstructor
@Component
class JwtTokenDecrypter {

    private final JwtProperty jwtProperty;

    AuthMember decrypt(String authHeader) {
        String token = validateTokenType(authHeader);
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtProperty.secretKey().getBytes());
        try {
            return new AuthMember(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                    .get("id", Long.class));
        } catch (ExpiredJwtException e) {
            throw new JwtTokenException(JwtTokenExceptionType.EXPIRED_JWT_TOKEN);
        } catch (JwtException e) {
            throw new JwtTokenException(JwtTokenExceptionType.INVALID_JWT_TOKEN);
        }
    }

    private String validateTokenType(String authHeader) {
        if(authHeader.startsWith(jwtProperty.bearerType())) {
            return authHeader.substring(jwtProperty.bearerType().length());
        }
        throw new JwtTokenException(JwtTokenExceptionType.NOT_BEARER_TYPE);
    }
}
