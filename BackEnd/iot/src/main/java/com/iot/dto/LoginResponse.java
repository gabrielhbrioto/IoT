package com.iot.dto;

/**
 * Classe que representa a resposta de login.
 */
public class LoginResponse {
    private String token; 
    private Long userId; 

    /**
     * Construtor da classe LoginResponse.
     * 
     * @param token Token de autenticação do usuário
     * @param userId ID do usuário
     */
    public LoginResponse(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
