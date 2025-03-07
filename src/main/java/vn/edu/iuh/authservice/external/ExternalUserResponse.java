package vn.edu.iuh.authservice.external;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * DTO for {@link vn.edu.iuh.userservice.models.User}
 */
public record ExternalUserResponse(UUID id, String username, String fullName, String email, String phone,
                                   String role, String address, String avatarUrl, Date birthdate,
                                   String gender) implements Serializable {
}