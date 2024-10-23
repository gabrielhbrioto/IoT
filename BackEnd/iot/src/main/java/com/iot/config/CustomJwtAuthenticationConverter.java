// package com.iot.config;

// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
// import org.springframework.stereotype.Component;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Mono;

// import java.util.Collections;

// @Component
// public class CustomJwtAuthenticationConverter implements ServerAuthenticationConverter {

//     private final JwtUtil jwtUtil;

//     public CustomJwtAuthenticationConverter(JwtUtil jwtUtil) {
//         this.jwtUtil = jwtUtil;
//     }

//     @Override
//     public Mono<Authentication> convert(ServerWebExchange exchange) {
//         String token = extractToken(exchange);
//         if (token != null) {
//             System.out.println("Token recebido: " + token);
//             String username = jwtUtil.extractUsername(token);
//             if (jwtUtil.validateToken(token, username)) {
//                 System.out.println("Token válido para o usuário: " + username);
//                 return Mono.just(new CustomJwtAuthentication(token, username, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
//             } else {
//                 System.out.println("Token inválido");
//             }
//         } else {
//             System.out.println("Token não encontrado");
//         }
//         return Mono.empty();
//     }


//     private String extractToken(ServerWebExchange exchange) {
//         String token = exchange.getRequest().getHeaders().getFirst("Authorization");
//         if (token != null && token.startsWith("Bearer ")) {
//             return token.substring(7); // Remove "Bearer " do token
//         }
//         return null;
//     }
// }
