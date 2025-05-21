package vn.edu.iuh.authservice.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.authservice.dtos.requests.CreateUserRequest;
import vn.edu.iuh.authservice.dtos.requests.UpdateUserRequest;
import vn.edu.iuh.authservice.dtos.requests.ChangePasswordRequest;
import vn.edu.iuh.authservice.dtos.responses.UserResponse;
import vn.edu.iuh.authservice.services.UserService;

import java.util.List;
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


   /////////////////
   @GetMapping("/username/{username}")
//   @PreAuthorize("hasRole('USER')")
   public ResponseEntity<UserResponse> getUserByUsername(@Valid @PathVariable String username) {
      return ResponseEntity.ok(userService.getUserByUsername(username));
   }


   @GetMapping("/phone/{phone}")
//   @PreAuthorize("hasRole('USER')")
   public ResponseEntity<UserResponse> getUserByPhone(@Valid @PathVariable String phone) {
      return ResponseEntity.ok(userService.getUserByPhone(phone));
   }

   @GetMapping("/email/{email}")
//   @PreAuthorize("hasRole('USER')")
   public ResponseEntity<UserResponse> getUserByEmail(@Valid @PathVariable String email) {
      return ResponseEntity.ok(userService.getUserByEmail(email));
   }

//   @PutMapping("/update")
////   @PreAuthorize("hasRole('USER')")
//   public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody User user) {
//      return ResponseEntity.ok(userService.updateUser(user));
//   }

   @PutMapping("/update")
   public ResponseEntity<UserResponse> updateUser(
           @Valid @RequestBody UpdateUserRequest userRequest) {
      // Parse id từ String sang UUID
      UUID id = UUID.fromString(userRequest.id());
      return ResponseEntity.ok(userService.updateUser(id, userRequest));
   }

   @PutMapping("/change-password")
   public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
      userService.changePassword(request);
      return ResponseEntity.noContent().build();
   }

   @DeleteMapping("/delete/{userId}")
//   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<Void> deleteUser(@Valid @PathVariable UUID userId) {
      userService.deleteUser(userId);
      return ResponseEntity.noContent().build();
   }

   @GetMapping("/all")
//   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<List<UserResponse>> getAllUser() {
      return ResponseEntity.ok(userService.getAllUser());
   }

   @GetMapping("/count")
   public ResponseEntity<Integer> getUserCount() {
      int count = userService.getAllUser().size();
      return ResponseEntity.ok(count);
   }
}

