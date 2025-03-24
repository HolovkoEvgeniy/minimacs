package com.lumiring.minimacs.config;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.util.Base64;

public class JwtSecrets {
    private static final String accessSecret;
    private static final String refreshSecret;

    static {
        accessSecret = generateSecretKey();
        refreshSecret = generateSecretKey();
    }

    public static String getAccessSecret() {
        return accessSecret;
    }

    public static String getRefreshSecret() {
        return refreshSecret;
    }

    private static String generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(256);
            Key key = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Jwt generation error", e);
        }
    }
}
