package com.iot.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe utilitária para manipulação de tokens JWT.
 */
@Component
public class JwtUtil {
    
    @Value("${app.secret.key}")
    private String secretKey;

    @Value("${app.jwt.expiration}")
    private String expirationTime;

    /**
     * Obtém a chave de assinatura a partir da chave secreta.
     * 
     * @return Key Chave de assinatura.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Gera um token JWT para um usuário específico.
     * 
     * @param username Nome de usuário.
     * @param userId ID do usuário.
     * @return String Token JWT gerado.
     */
    public String generateToken(String username, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return createToken(claims, username);
    }

    /**
     * Cria um token JWT com as reivindicações e o assunto fornecidos.
     * 
     * @param claims Reivindicações a serem incluídas no token.
     * @param subject Assunto do token (normalmente o nome de usuário).
     * @return String Token JWT criado.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        long expirationMillis = Long.parseLong(expirationTime);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida um token JWT comparando o nome de usuário e verificando a expiração.
     * 
     * @param token Token JWT a ser validado.
     * @param username Nome de usuário a ser comparado.
     * @return boolean true se o token for válido, false caso contrário.
     */
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * Extrai o nome de usuário de um token JWT.
     * 
     * @param token Token JWT.
     * @return String Nome de usuário extraído.
     */
    public String extractUsername(String token) {
        if (token == null) {
            return null;
        }
        return extractAllClaims(token).getSubject();
    }
    
    /**
     * Extrai o ID do usuário de um token JWT.
     * 
     * @param token Token JWT.
     * @return Long ID do usuário extraído.
     */
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return Long.valueOf(claims.get("userId").toString());
    }

    /**
     * Extrai todas as reivindicações de um token JWT.
     * 
     * @param token Token JWT.
     * @return Claims Reivindicações extraídas.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verifica se um token JWT está expirado.
     * 
     * @param token Token JWT.
     * @return boolean true se o token estiver expirado, false caso contrário.
     */
    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }
}
