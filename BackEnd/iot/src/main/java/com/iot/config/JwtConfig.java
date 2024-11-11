package com.iot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtConfig {

    @Value("${app.secret.key}")
    private String secretKey;
    
    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        // Converte a chave secreta para SecretKey
        SecretKey secretKeyBytes = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        // Configura o JwtDecoder usando a chave secreta
        return NimbusReactiveJwtDecoder.withSecretKey(secretKeyBytes).build();
    }
}
