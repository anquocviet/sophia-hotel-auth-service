package vn.edu.iuh.authservice.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import vn.edu.iuh.authservice.enums.Gender;
import vn.edu.iuh.authservice.enums.Role;


import java.sql.Timestamp;
import java.util.Date;

public record UpdateUserRequest(
        @NotBlank(message = "Id is required") String id,
        @NotBlank(message = "Name is required") String fullName,
        @NotBlank(message = "Username is required") String username,
        String password,
        @NotBlank(message = "Email is required") String email,

        String phone,
        String address,
        String avatarUrl,
        Date birthdate,
        Gender gender,
        Role role,
        Timestamp createdAt,
        Timestamp updatedAt,
        Timestamp deletedAt
) {
}
