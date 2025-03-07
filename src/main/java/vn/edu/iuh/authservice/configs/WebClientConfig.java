package vn.edu.iuh.authservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @description
 * @author: vie
 * @date: 26/2/25
 */
@Configuration
public class WebClientConfig {
   @Bean
   public WebClient webClient() {
      return WebClient.builder().build();
   }
}
