package com.springbooks.library.util;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Base64;

@Component
public class JwtTestTokenGenerator {

    public static String generateTestToken() {
        String header = """
            {
              "alg": "RS256",
              "typ": "JWT"
            }
            """;
        
        long now = Instant.now().getEpochSecond();
        long exp = now + 3600;
        
        String payload = String.format("""
            {
              "sub": "test-user",
              "aud": "springbooks-api",
              "iss": "https://dev-example.auth0.com/",
              "exp": %d,
              "iat": %d,
              "scope": "read write"
            }
            """, exp, now);
        
        String encodedHeader = Base64.getUrlEncoder().withoutPadding()
            .encodeToString(header.getBytes());
        String encodedPayload = Base64.getUrlEncoder().withoutPadding()
            .encodeToString(payload.getBytes());
        
        String signature = "test-signature";
        String encodedSignature = Base64.getUrlEncoder().withoutPadding()
            .encodeToString(signature.getBytes());
        
        return encodedHeader + "." + encodedPayload + "." + encodedSignature;
    }

    public static void main(String[] args) {
        System.out.println("Test JWT Token for SpringBooks API:");
        System.out.println(generateTestToken());
        System.out.println("\nCopy this token and use it in Swagger UI Authorization header.");
    }
}
