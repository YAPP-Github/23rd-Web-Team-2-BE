package com.baro.auth.application;

import com.baro.auth.domain.Token;

public interface TokenTranslator {

    Token encode(Long id, String ipAddress);
    Long decodeAccessToken(String token);
    String decodeRefreshToken(String token);
}
