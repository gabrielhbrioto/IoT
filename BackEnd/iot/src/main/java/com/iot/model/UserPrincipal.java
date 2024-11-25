package com.iot.model;

/**
 * Representa o usuário principal com ID e email.
 */
public class UserPrincipal {
    private final Long userId;
    private final String email; 

    /**
     * Construtor para inicializar UserPrincipal com ID e email.
     *
     * @param userId ID do usuário
     * @param email Email do usuário
     */
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
        return this.email;
    }
}
