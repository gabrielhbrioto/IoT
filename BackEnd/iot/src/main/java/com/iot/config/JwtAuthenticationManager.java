package com.iot.config;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import reactor.core.publisher.Mono;

public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationManager(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();
        if (token != null && jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.extractUsername(token));
            return Mono.just(new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities()));
        }
        return Mono.empty();
    }
}
