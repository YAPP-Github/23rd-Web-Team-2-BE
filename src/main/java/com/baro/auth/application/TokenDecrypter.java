package com.baro.auth.application;

public interface TokenDecrypter {

    Long decrypt(String authHeader);
}
