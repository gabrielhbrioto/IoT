// // package com.iot.config;

// // import org.springframework.context.annotation.Configuration;
// // import org.springframework.web.reactive.config.CorsRegistry;
// // import org.springframework.web.reactive.config.WebFluxConfigurer;

// // @Configuration
// // public class CorsConfig implements WebFluxConfigurer {

// //     @Override
// //     public void addCorsMappings(CorsRegistry registry) {
// //         registry.addMapping("/**")
// //                 .allowedOrigins("http://localhost:3000") // ou qualquer origem que você desejar
// //                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
// //     }
// // }
// package com.iot.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.reactive.config.CorsRegistry;
// import org.springframework.web.reactive.config.WebFluxConfigurer;

// @Configuration
// public class CorsConfig implements WebFluxConfigurer {

//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         System.out.println("Configurando CORS...");
//         registry.addMapping("/**")
//                 .allowedOrigins("*") // Especifique a origem exata
//                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                 .allowedHeaders("*")  // Permite todos os cabeçalhos
//                 .allowCredentials(true); // Ajuste conforme necessário
//     }
// }

