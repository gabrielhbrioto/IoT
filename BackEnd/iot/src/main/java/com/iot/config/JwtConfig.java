package com.iot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Configuração para decodificação de JWT (JSON Web Token).
 * 
 * Esta classe configura um decodificador JWT reativo usando uma chave secreta.
 */
@Configuration
public class JwtConfig {

    /**
     * A chave secreta usada para assinar e verificar tokens JWT.
     * 
     * Valor injetado a partir das propriedades da aplicação.
     */
    @Value("${app.secret.key}")
    private String secretKey;
    
    /**
     * Cria um bean de ReactiveJwtDecoder configurado com a chave secreta.
     * 
     * @return um decodificador JWT reativo configurado.
     */
    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        // Converte a chave secreta para SecretKey
        SecretKey secretKeyBytes = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        // Configura o JwtDecoder usando a chave secreta
        return NimbusReactiveJwtDecoder.withSecretKey(secretKeyBytes).build();
    }
}
