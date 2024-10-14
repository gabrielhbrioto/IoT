package com.iot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Table("USUARIO")
public class Usuario implements UserDetails {

    @Id
    private Long id;

    private String email;

    private String senha;

    private String nome;

    // Getter e Setter para o ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter e Setter para o Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter e Setter para a Senha
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    // Getter e Setter para o Nome
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Implementações da interface UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorne uma coleção de autoridades. Aqui você pode retornar um conjunto de autoridades.
        // Por enquanto, retornando uma coleção vazia, você pode modificar conforme necessário.
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email; // Usando o email como nome de usuário
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Ajuste conforme necessário
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Ajuste conforme necessário
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Ajuste conforme necessário
    }

    @Override
    public boolean isEnabled() {
        return true; // Ajuste conforme necessário
    }
}
