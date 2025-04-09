package vn.edu.iuh.authservice.services;

import org.springframework.stereotype.Service;
import vn.edu.iuh.authservice.dtos.requests.CreateUserRequest;
import vn.edu.iuh.authservice.dtos.responses.UserResponse;
import vn.edu.iuh.authservice.models.User;

import java.util.UUID;

@Service
public interface UserService {
   UserResponse createUser(CreateUserRequest userRequest);
   UserResponse getUserById(UUID id);
   UserResponse getUserByUsername(String username);
   UserResponse getUserByPhone(String phone);
   UserResponse getUserByEmail(String email);
   UserResponse updateUser(User user);
   void deleteUser(UUID id);
   boolean isOwner(String username, UUID userId);
}
