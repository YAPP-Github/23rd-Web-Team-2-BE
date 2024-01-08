package com.baro.auth.fake.jwt;

import com.baro.auth.application.TokenDecrypter;

public class FakeTokenDecrypter implements TokenDecrypter {

    private final Long decryptedId;
    private final String decryptedIpAddress;

    public FakeTokenDecrypter(Long decryptedId, String decryptedIpAddress) {
        this.decryptedId = decryptedId;
        this.decryptedIpAddress = decryptedIpAddress;
    }

    @Override
    public Long decryptAccessToken(String authorization) {
        return decryptedId;
    }

    @Override
    public String decryptRefreshToken(String authorization) {
        return decryptedIpAddress;
    }
}
