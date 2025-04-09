package vn.edu.iuh.authservice.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.authservice.dtos.requests.CreateUserRequest;
import vn.edu.iuh.authservice.dtos.requests.LoginRequest;
import vn.edu.iuh.authservice.dtos.responses.LoginResponse;
import vn.edu.iuh.authservice.services.AuthService;
import vn.edu.iuh.authservice.services.impl.AuthServiceImpl;

/**
 * @description
 * @author: vie
 * @date: 26/2/25
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
   private final AuthService authService;

   public AuthController(AuthServiceImpl authService) {
      this.authService = authService;
   }

   @PostMapping("/login")
   public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
      LoginResponse response = authService.login(loginRequest);
      return ResponseEntity.ok(response);
   }

   @PostMapping("/register")
   public ResponseEntity<Void> register(@Valid @RequestBody CreateUserRequest createUserRequest) {
      authService.register(createUserRequest);
      return new ResponseEntity<>(HttpStatus.CREATED);
   }
}
