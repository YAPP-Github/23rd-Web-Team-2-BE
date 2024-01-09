package com.baro.auth.infra.jwt;

public interface IdentifierTranslator {

    String encode(String input);

    String decode(String cipher);
}
