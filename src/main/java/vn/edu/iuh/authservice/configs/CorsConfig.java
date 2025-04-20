package vn.edu.iuh.authservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

   @Override
   public void addCorsMappings(CorsRegistry registry) {
         // For API docs endpoints - no credentials needed
         registry.addMapping("/v3/api-docs/**")
               .allowedOrigins("*")
               .allowedMethods("GET")
               .allowedHeaders("*")
               .maxAge(3600);
               
         registry.addMapping("/api/v1/v3/api-docs/**")
               .allowedOrigins("*")
               .allowedMethods("GET")
               .allowedHeaders("*")
               .maxAge(3600);
               
         registry.addMapping("/swagger-ui/**")
               .allowedOrigins("*")
               .allowedMethods("GET")
               .allowedHeaders("*")
               .maxAge(3600);
   
         // For authenticated endpoints - with credentials
         registry.addMapping("/auth/**")
               .allowedOriginPatterns("*")
               .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
               .allowedHeaders("*")
               .exposedHeaders("Authorization")
               .allowCredentials(true)
               .maxAge(3600);
               
         // For all other endpoints
      registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("Authorization")
            .allowCredentials(true)
            .maxAge(3600);
   }

   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
      // Configuration for API docs and Swagger UI - no credentials
      CorsConfiguration docsConfiguration = new CorsConfiguration();
      docsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
      docsConfiguration.setAllowedMethods(Arrays.asList("GET", "OPTIONS"));
      docsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
      docsConfiguration.setMaxAge(3600L);
   
      // Configuration for authenticated endpoints - with credentials
      CorsConfiguration authConfiguration = new CorsConfiguration();
      authConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
      authConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
      authConfiguration.setAllowedHeaders(Collections.singletonList("*"));
      authConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));
      authConfiguration.setAllowCredentials(true);
      authConfiguration.setMaxAge(3600L);
   
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/v3/api-docs/**", docsConfiguration);
      source.registerCorsConfiguration("/api/v1/v3/api-docs/**", docsConfiguration);
      source.registerCorsConfiguration("/swagger-ui/**", docsConfiguration);
      source.registerCorsConfiguration("/auth/**", authConfiguration);
      source.registerCorsConfiguration("/**", authConfiguration);
      return source;
   }
}