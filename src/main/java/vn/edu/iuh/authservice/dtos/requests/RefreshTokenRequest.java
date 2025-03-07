package vn.edu.iuh.authservice.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
      @NotBlank(message = "Refresh token is required") String refreshToken) {
}
