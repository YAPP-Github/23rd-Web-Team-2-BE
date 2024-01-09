package com.baro.auth.fake.jwt;

import com.baro.auth.infra.jwt.IdentifierTranslator;

public class FakeIdentifierTranslator implements IdentifierTranslator {

    private final String encodedCode;
    private final String decodedCode;

    public FakeIdentifierTranslator(String encodedCode, String decodedCode) {
        this.encodedCode = encodedCode;
        this.decodedCode = decodedCode;
    }

    @Override
    public String encode(String input) {
        return encodedCode;
    }

    @Override
    public String decode(String cipher) {
        return decodedCode;
    }
}
