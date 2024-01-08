package com.baro.auth.application;

public interface TokenDecrypter {

    Long decryptAccessToken(String accessToken);
    String decryptRefreshToken(String refreshToken);
}
