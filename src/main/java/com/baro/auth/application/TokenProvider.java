package com.baro.auth.application;

import com.baro.auth.domain.Token;
import com.baro.member.domain.Member;

public interface TokenProvider {
    Token provide(Member member);
}
