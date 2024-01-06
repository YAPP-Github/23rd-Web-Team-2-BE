package com.baro.auth.infra.jwt;

public interface TokenDecrypter {

    Long decrypt(String authHeader);
}
