package vn.edu.iuh.authservice.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.authservice.dtos.responses.UserResponse;
import vn.edu.iuh.authservice.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
   private final UserService userService;

   public UserController(UserService userService) {
      this.userService = userService;
   }

   @GetMapping("/id/{userId}")
   public ResponseEntity<UserResponse> getUsers(@Valid @PathVariable UUID userId) {
      return ResponseEntity.ok(userService.getUserById(userId));
   }
}
