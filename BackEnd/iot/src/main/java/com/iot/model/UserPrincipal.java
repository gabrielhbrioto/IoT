package com.iot.model;

public class UserPrincipal {
    private final Long userId;
    private final String email;

    public UserPrincipal(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return this.email; // Aqui você define como ele será exibido como principal
    }
}
