package com.iot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String testConnection() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return "Conexão com o banco de dados bem-sucedida!";
        } catch (Exception e) {
            return "Erro ao conectar com o banco de dados: " + e.getMessage();
        }
    }
}
