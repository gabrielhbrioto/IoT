// package com.iot.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.builders.WebSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .authorizeRequests()
//                 .antMatchers("/public/**").permitAll() // Acesso público
//                 .anyRequest().authenticated() // Requer autenticação
//                 .and()
//             .formLogin() // Configuração de login
//                 .permitAll()
//                 .and()
//             .logout()
//                 .permitAll();
//         return http.build();
//     }

//     @Bean
//     public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//         AuthenticationManagerBuilder authenticationManagerBuilder = 
//             http.getSharedObject(AuthenticationManagerBuilder.class);
//         // Configurações de autenticação (usuários, senhas, etc.)
//         return authenticationManagerBuilder.build();
//     }
// }
