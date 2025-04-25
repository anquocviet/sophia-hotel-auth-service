package vn.edu.iuh.authservice.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ChangePasswordRequest(
        @NotNull(message = "User ID is required") UUID userId,
        @NotBlank(message = "Old password is required") String oldPassword,
        @NotBlank(message = "New password is required") String newPassword
) {}
