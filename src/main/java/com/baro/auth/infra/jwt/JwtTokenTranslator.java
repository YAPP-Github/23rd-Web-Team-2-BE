package com.baro.auth.infra.jwt;

import com.baro.auth.application.TokenTranslator;
import com.baro.auth.domain.Token;
import com.baro.common.time.TimeServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenTranslator implements TokenTranslator {

    private final TimeServer timeServer;
    private final JwtTokenCreator jwtTokenCreator;
    private final JwtTokenDecrypter jwtTokenDecrypter;

    @Override
    public Token encode(Long id) {
        return jwtTokenCreator.createToken(id, timeServer.now());
    }

    @Override
    public Long decode(String token) {
        return jwtTokenDecrypter.decrypt(token);
    }
}
