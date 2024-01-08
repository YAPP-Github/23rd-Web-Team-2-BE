package com.baro.auth.infra.jwt;

import com.baro.auth.application.TokenCreator;
import com.baro.auth.application.TokenDecrypter;
import com.baro.auth.application.TokenTranslator;
import com.baro.auth.domain.Token;
import com.baro.common.time.TimeServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenTranslator implements TokenTranslator {

    private final TimeServer timeServer;
    private final TokenCreator tokenCreator;
    private final TokenDecrypter tokenDecrypter;

    @Override
    public Token encode(Long id, String ipAddress) {
        return tokenCreator.createToken(id, ipAddress, timeServer.now());
    }

    @Override
    public Long decodeAccessToken(String token) {
        return tokenDecrypter.decryptAccessToken(token);
    }

    @Override
    public String decodeRefreshToken(String token) {
        return tokenDecrypter.decryptRefreshToken(token);
    }
}
