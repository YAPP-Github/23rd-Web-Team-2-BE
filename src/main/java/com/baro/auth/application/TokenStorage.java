package com.baro.auth.application;

public interface TokenStorage {

    void saveRefreshToken(String key, String value);
    String findRefreshToken(String key);
}
