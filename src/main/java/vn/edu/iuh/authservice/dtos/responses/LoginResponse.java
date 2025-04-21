package vn.edu.iuh.authservice.dtos.responses;

import vn.edu.iuh.authservice.enums.Role;
import java.util.UUID;

/**
 * @description
 * @author: vie
 * @date: 26/2/25
 */
public record LoginResponse(
      String accessToken,
      UUID id,
      Role role
) {
}
