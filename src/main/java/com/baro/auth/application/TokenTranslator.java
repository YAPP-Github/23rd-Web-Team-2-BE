package com.baro.auth.application;

import com.baro.auth.domain.Token;

public interface TokenTranslator {

    Token encode(Long id);
    Long decode(String token);
}
