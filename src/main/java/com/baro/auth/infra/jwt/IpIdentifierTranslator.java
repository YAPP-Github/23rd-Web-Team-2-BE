package com.baro.auth.infra.jwt;

import com.baro.auth.exception.AuthException;
import com.baro.auth.exception.AuthExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequiredArgsConstructor
@Component
public class IpIdentifierTranslator implements IdentifierTranslator {

    private final JwtProperty jwtProperty;

    @Override
    public String encode(String input) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(jwtProperty.ipSecretKey().getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedByteValue = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedByteValue);
        } catch (Exception e) {
            throw new AuthException(AuthExceptionType.ENCRYPTION_ERROR);
        }
    }

    @Override
    public String decode(String code) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(jwtProperty.ipSecretKey().getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedValue64 = Base64.getDecoder().decode(code);
            byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
            return new String(decryptedByteValue, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new AuthException(AuthExceptionType.ENCRYPTION_ERROR);
        }
    }
}
