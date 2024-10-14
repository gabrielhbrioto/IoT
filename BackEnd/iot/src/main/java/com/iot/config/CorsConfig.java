// package com.iot.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.reactive.config.CorsRegistry;
// import org.springframework.web.reactive.config.WebFluxConfigurer;

// @Configuration
// public class CorsConfig implements WebFluxConfigurer {

//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//                 .allowedOrigins("http://localhost:3000") // ou qualquer origem que você desejar
//                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
//     }
// }
package com.iot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Permite todas as origens, incluindo o Postman
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")  // Permite todos os cabeçalhos
                .allowCredentials(false); // Desabilita envio de credenciais (ajuste conforme necessário)
    }
}
