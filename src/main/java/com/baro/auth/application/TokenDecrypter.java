package com.baro.auth.application;

public interface TokenDecrypter {

    Long decryptAccessToken(String accessToken);
    void decryptRefreshToken(String refreshToken);
}
