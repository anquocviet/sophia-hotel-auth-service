package vn.edu.iuh.authservice.services;

import org.springframework.stereotype.Service;
import vn.edu.iuh.authservice.dtos.requests.CreateUserRequest;
import vn.edu.iuh.authservice.dtos.requests.UpdateUserRequest;
import vn.edu.iuh.authservice.dtos.responses.UserResponse;
import vn.edu.iuh.authservice.models.User;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
   UserResponse getUserById(UUID id);
   UserResponse getUserByUsername(String username);
   UserResponse getUserByPhone(String phone);
   UserResponse getUserByEmail(String email);
//   UserResponse updateUser(User user);
   UserResponse updateUser(UUID id, UpdateUserRequest userRequest);

   void deleteUser(UUID id);
   boolean isOwner(String username, UUID userId);
    List<UserResponse> getAllUser();
}
