package com.iot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Classe de configuração para codificação de senhas.
 * 
 * Esta classe define um bean para o codificador de senhas usando o algoritmo BCrypt.
 */
@Configuration
public class PasswordConfig {

    /**
     * Define um bean para o codificador de senhas.
     * 
     * @return uma instância de BCryptPasswordEncoder para codificação de senhas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
