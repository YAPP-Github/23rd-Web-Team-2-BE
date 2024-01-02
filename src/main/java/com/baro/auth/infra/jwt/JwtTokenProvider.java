package com.baro.auth.infra.jwt;

import com.baro.auth.application.TokenProvider;
import com.baro.auth.domain.Token;
import com.baro.common.time.TimeServer;
import com.baro.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenProvider {

    private final TimeServer timeServer;
    private final JwtTokenCreator jwtTokenCreator;

    @Override
    public Token provide(Member member) {
        return jwtTokenCreator.createToken(member, timeServer.now());
    }
}
