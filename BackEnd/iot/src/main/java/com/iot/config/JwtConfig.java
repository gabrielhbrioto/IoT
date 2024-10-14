package com.iot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtConfig {

    private final String SECRET_KEY = "your_secret_key";  // Substitua pela sua chave secreta

    @Bean
    public JwtDecoder jwtDecoder() {
        // Converte a chave secreta para SecretKey
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
        // Configura o JwtDecoder usando a chave secreta
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }
}
