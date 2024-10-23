// package com.iot.config;

// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;

// import java.util.Collection;

// public class CustomJwtAuthentication implements Authentication {
//     private final String token;
//     private final String username;
//     private final Collection<? extends GrantedAuthority> authorities; // Para as roles do usuário
//     private boolean authenticated = true; // Modifique conforme necessário

//     // Construtor que aceita token e username
//     public CustomJwtAuthentication(String token, String username, Collection<? extends GrantedAuthority> authorities) {
//         this.token = token;
//         this.username = username;
//         this.authorities = authorities; // Incluir authorities, se necessário
//     }

//     @Override
//     public String getName() {
//         return username;
//     }

//     @Override
//     public Collection<? extends GrantedAuthority> getAuthorities() {
//         return authorities;
//     }

//     @Override
//     public Object getCredentials() {
//         return token; // Retorna o token como credencial
//     }

//     @Override
//     public Object getDetails() {
//         return null; // Implementar se necessário
//     }

//     @Override
//     public Object getPrincipal() {
//         return username; // Retorna o nome de usuário
//     }

//     @Override
//     public boolean isAuthenticated() {
//         return authenticated; // Retorna se está autenticado
//     }

//     @Override
//     public void setAuthenticated(boolean authenticated) {
//         this.authenticated = authenticated; // Define se está autenticado
//     }
// }
