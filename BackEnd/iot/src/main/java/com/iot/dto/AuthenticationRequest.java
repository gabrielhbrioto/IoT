package com.iot.dto;

public class AuthenticationRequest {

    private String username;
    private String password;

    // Construtor vazio
    public AuthenticationRequest() {
    }

    // Construtor com parâmetros
    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters e setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
