package vn.edu.iuh.authservice.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
      @NotBlank(message = "Name is required") String fullName,
      @NotBlank(message = "Username is required") String username,
      @NotBlank(message = "Password is required") String password,
      @NotBlank(message = "Email is required") String email,
      @NotBlank(message = "Role is required") String role
) {
}
