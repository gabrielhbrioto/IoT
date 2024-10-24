// package com.iot.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.ReactiveAuthenticationManager;
// import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
// import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
// import org.springframework.security.crypto.password.PasswordEncoder;

// @Configuration
// public class AuthConfig {

//     private final ReactiveUserDetailsService userDetailsService;
//     private final PasswordEncoder passwordEncoder;

//     public AuthConfig(ReactiveUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
//         this.userDetailsService = userDetailsService;
//         this.passwordEncoder = passwordEncoder;
//     }

//     @Bean
//     public ReactiveAuthenticationManager authenticationManager() {
//         UserDetailsRepositoryReactiveAuthenticationManager authManager =
//                 new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
//         authManager.setPasswordEncoder(passwordEncoder); // Certifique-se de que está usando o mesmo encoder
//         return authManager;
//     }
// }
