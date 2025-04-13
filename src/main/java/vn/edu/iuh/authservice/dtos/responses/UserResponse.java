package vn.edu.iuh.authservice.dtos.responses;


import vn.edu.iuh.authservice.enums.Gender;
import vn.edu.iuh.authservice.enums.Role;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * DTO for {@link vn.edu.iuh.authservice.models.User}
 */
public record UserResponse(
        UUID id,
        String username,
        String fullName,
        String email,
        String phone,
        Role role,
        String address,
        String avatarUrl,
        Date birthdate,
        Gender gender,
        Timestamp createdAt,
        Timestamp updatedAt,
        Timestamp deletedAt

) implements Serializable {
}