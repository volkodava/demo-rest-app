package com.demo.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {

    private static final String MAGIC_KEY = "MyMagicKey-right_here";

    @Autowired(required = true)
    @Qualifier("passwordEncoder")
    private PasswordEncoder passwordEncoder;

    public String createToken(UserDetails userDetails) {

        // Expires in one hour
        long expires = System.currentTimeMillis() + 1000L * 60 * 60;

        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(userDetails.getUsername());
        tokenBuilder.append(":");
        tokenBuilder.append(expires);
        tokenBuilder.append(":");
        tokenBuilder.append(createEncryptedSignature(userDetails, expires));

        return tokenBuilder.toString();
    }

    public String getUserNameFromToken(String authToken) {

        if (null == authToken) {
            return null;
        }

        String[] parts = authToken.split(":");
        return parts[0];
    }

    public boolean validateToken(String authToken, UserDetails userDetails) {

        String[] parts = authToken.split(":");
        long expires = Long.parseLong(parts[1]);
        String encryptedSignature = parts[2];

        if (expires < System.currentTimeMillis()) {
            return false;
        }

        String rawSignature = createRawSignature(userDetails, expires);
        boolean isValid = passwordEncoder.matches(rawSignature, encryptedSignature);
        return isValid;
    }

    private String createEncryptedSignature(UserDetails userDetails, long expires) {

        String rawSignature = createRawSignature(userDetails, expires);

        return passwordEncoder.encode(rawSignature);
    }

    private String createRawSignature(UserDetails userDetails, long expires) {

        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(userDetails.getUsername());
        signatureBuilder.append(":");
        signatureBuilder.append(expires);
        signatureBuilder.append(":");
        signatureBuilder.append(TokenUtils.MAGIC_KEY);

        return signatureBuilder.toString();
    }
}
