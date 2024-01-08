package com.baro.auth.fake.jwt;

import com.baro.auth.application.TokenDecrypter;

public class FakeTokenDecrypter implements TokenDecrypter {

    private final Long decryptedResult;

    public FakeTokenDecrypter(Long decryptedResult) {
        this.decryptedResult = decryptedResult;
    }

    @Override
    public Long decrypt(String authHeader) {
        return decryptedResult;
    }
}
