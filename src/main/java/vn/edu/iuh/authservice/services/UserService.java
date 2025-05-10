package vn.edu.iuh.authservice.services;

import org.springframework.stereotype.Service;
import vn.edu.iuh.authservice.dtos.requests.ChangePasswordRequest;
import vn.edu.iuh.authservice.dtos.requests.UpdateUserRequest;
import vn.edu.iuh.authservice.dtos.responses.UserResponse;

import java.util.List;
import java.util.UUID;

/**
 * @description User management service interface
 * @author: vie
 * @date: 14/2/25
 */
@Service
public interface UserService {
   UserResponse getUserById(UUID id);
   UserResponse getUserByUsername(String username);
   UserResponse getUserByPhone(String phone);
   UserResponse getUserByEmail(String email);
   UserResponse updateUser(UUID id, UpdateUserRequest userRequest);
   void deleteUser(UUID id);
   boolean isOwner(String username, UUID userId);
   List<UserResponse> getAllUser();
   void changePassword(ChangePasswordRequest request);
}
