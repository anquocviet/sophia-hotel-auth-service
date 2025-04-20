package vn.edu.iuh.authservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import vn.edu.iuh.authservice.filters.JWTAuthenticationFilter;
import vn.edu.iuh.authservice.utils.JWTUtil;

/**
 * @description
 * @author: vie
 * @date: 26/2/25
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

   private final JWTUtil jwtUtil;
   private final UserDetailsService userDetailsService;
   private final CorsConfigurationSource corsConfigurationSource;

   public SecurityConfig(JWTUtil jwtUtil, UserDetailsService userDetailsService, CorsConfigurationSource corsConfigurationSource) {
      this.jwtUtil = jwtUtil;
      this.userDetailsService = userDetailsService;
      this.corsConfigurationSource = corsConfigurationSource;
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean
   public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
      AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
      authenticationManagerBuilder.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
      return authenticationManagerBuilder.build();
   }

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http.csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                  .requestMatchers(
                        "/auth/**", 
                        "/v3/api-docs/**",
                        "/api/v1/v3/api-docs/**",
                        "/swagger-ui.html", 
                        "/swagger-ui/**"
                  ).permitAll()
                  .anyRequest().authenticated()
            )
            .addFilterBefore(new JWTAuthenticationFilter(userDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(
                  session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

      return http.build();
   }
}
