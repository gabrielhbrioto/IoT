package com.iot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;

/**
 * Configura o banco de dados usando parâmetros do arquivo de propriedades da aplicação.
 * 
 * Os parâmetros são importados com @Value e usados para criar uma ConnectionFactory para PostgreSQL.
 */

@Configuration
public class DatabaseConfig {

/**
 * A anotação @Value é usada para injetar valores das propriedades definidas no arquivo de 
 * configuração da aplicação.
 */
    @Value("${spring.r2dbc.host}")
    private String host;

    @Value("${spring.r2dbc.port}")
    private int port;

    @Value("${spring.r2dbc.database}")
    private String database;

    @Value("${spring.r2dbc.username}")
    private String username;

    @Value("${spring.r2dbc.password}")
    private String password;

    /**
     * Cria e configura uma instância de ConnectionFactory para conexão com o banco de dados PostgreSQL.
     *
     * @return uma instância configurada de PostgresqlConnectionFactory
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host(host)
                .port(port)
                .database(database)
                .username(username)
                .password(password)
                .build());
    }
}
