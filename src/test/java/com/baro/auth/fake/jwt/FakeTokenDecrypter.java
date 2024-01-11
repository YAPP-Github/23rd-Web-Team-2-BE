package com.baro.auth.fake.jwt;

import com.baro.auth.application.TokenDecrypter;

public class FakeTokenDecrypter implements TokenDecrypter {

    @Override
    public Long decryptAccessToken(String token) {
        return 1L;
    }

    @Override
    public void decryptRefreshToken(String token) {
    }
}
