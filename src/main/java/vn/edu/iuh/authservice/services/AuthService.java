package vn.edu.iuh.authservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import vn.edu.iuh.authservice.dtos.requests.LoginRequest;
import vn.edu.iuh.authservice.dtos.requests.RefreshTokenRequest;
import vn.edu.iuh.authservice.dtos.requests.UserRegisterRequest;
import vn.edu.iuh.authservice.dtos.responses.LoginResponse;
import vn.edu.iuh.authservice.exceptions.impl.FailCreateUser;
import vn.edu.iuh.authservice.external.ExternalUserResponse;

/**
 * @description
 * @author: vie
 * @date: 26/2/25
 */
@Slf4j
@Service
public class AuthService {
   @Value("${keycloak.url}")
   private String keyCloakUrl;
   @Value("${keycloak.realm}")
   private String realm;
   @Value("${keycloak.resource}")
   private String resource;
   @Value("${keycloak.username}")
   private String username;
   @Value("${keycloak.password}")
   private String password;

   private final WebClient webClient;

   public AuthService(WebClient webClient) {
      this.webClient = webClient;
   }

   public LoginResponse login(LoginRequest loginRequest) {
      String url = String.format("%s/realms/%s/protocol/openid-connect/token", keyCloakUrl, realm);
      String request = String.format("client_id=%s&username=%s&password=%s&grant_type=password", resource, loginRequest.username(), loginRequest.password());
      String response = sendPostRequest(url, request).getBody();
      return parseTokenResponse(response);
   }

   public LoginResponse refreshToken(RefreshTokenRequest tokenRequest) {
      String url = String.format("%s/realms/%s/protocol/openid-connect/token", keyCloakUrl, realm);
      String request = String.format("client_id=%s&refresh_token=%s&grant_type=refresh_token", resource, tokenRequest.refreshToken());
      String response = sendPostRequest(url, request).getBody();
      return parseTokenResponse(response);
   }

   public void registerUser(UserRegisterRequest user) {
      String adminAccessToken = getAdminAccessToken();
      ResponseEntity<ExternalUserResponse> userResponse = webClient.post()
            .uri("http://user-service:8082/api/v1/users/create")
            .bodyValue(user)  // Use bodyValue instead of body
            .retrieve()
            .toEntity(ExternalUserResponse.class)
            .block();

      if (userResponse != null && userResponse.getStatusCode() == HttpStatus.CREATED) {
         // Keycloak API to create user
         String url = String.format("%s/admin/realms/%s/users", keyCloakUrl, realm);
         String userJson = String.format("""
               {
                 "id": "%s",
                 "username": "%s",
                 "enabled": true,
                 "email": "%s",
                 "firstName": "%s",
                 "lastName": "User",
                 "credentials": [
                   {
                     "type": "password",
                     "value": "%s",
                     "temporary": false
                   }
                 ]
               }
               """, userResponse.getBody().id(), user.username(), user.email(), user.fullName(), user.password());
         sendPostRequestWithAuth(url, userJson, adminAccessToken);
         return;
      }
      throw new FailCreateUser();
   }

   private ResponseEntity<String> sendPostRequest(String url, String requestBody) {
      return webClient.post()
            .uri(url)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .bodyValue(requestBody)
            .retrieve()
            .toEntity(String.class)
            .block();
   }

   private ResponseEntity<String> sendPostRequestWithAuth(String url, String requestBody, String accessToken) {
      return webClient.post()
            .uri(url)
            .header("Authorization", "Bearer " + accessToken)
            .header("Content-Type", "application/json")
            .bodyValue(requestBody)
            .retrieve()
            .toEntity(String.class)
            .block();
   }

   private String getAdminAccessToken() {
      String url = String.format("%s/realms/%s/protocol/openid-connect/token", keyCloakUrl, "master");
      String requestBody = String.format("client_id=%s&username=%s&password=%s&grant_type=password", resource, username, password);

      ResponseEntity<String> response = sendPostRequest(url, requestBody);

      if (response != null && response.getStatusCode() == HttpStatus.OK) {
         return parseTokenResponse(response.getBody()).accessToken();
      } else {
         return null;
      }
   }


   private LoginResponse parseTokenResponse(String responseBody) {
      try {
         ObjectMapper objectMapper = new ObjectMapper();
         JsonNode jsonNode = objectMapper.readTree(responseBody);
         String accessToken = jsonNode.path("access_token").asText();
         String refreshToken = jsonNode.path("refresh_token").asText();
         String tokenType = jsonNode.path("token_type").asText();
         int expiresIn = jsonNode.path("expires_in").asInt();

         return new LoginResponse(accessToken, refreshToken, tokenType, expiresIn);
      } catch (JsonProcessingException e) {
         e.printStackTrace();
         return null;
      }
   }

}
