package com.baro.auth.fake.jwt;

import com.baro.auth.infra.jwt.TokenDecrypter;

public class FakeTokenDecrypter implements TokenDecrypter {

    private Long decryptedResult;

    public FakeTokenDecrypter(Long decryptedResult) {
        this.decryptedResult = decryptedResult;
    }

    @Override
    public Long decrypt(String authHeader) {
        return decryptedResult;
    }
}
