package com.baro.auth.application;

import com.baro.auth.domain.Token;

import java.time.Instant;

public interface TokenCreator {

    Token createToken(Long id, Instant now);
}
