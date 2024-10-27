package com.iot.dto;

public class LoginResponse {
    private String token;
    private Long userId; // Ou o tipo correspondente ao ID do usuário

    public LoginResponse(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    // Getters e Setters
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
