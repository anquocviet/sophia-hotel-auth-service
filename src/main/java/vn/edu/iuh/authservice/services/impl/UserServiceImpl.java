package vn.edu.iuh.authservice.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.iuh.authservice.dtos.requests.CreateUserRequest;
import vn.edu.iuh.authservice.dtos.requests.UpdateUserRequest;
import vn.edu.iuh.authservice.dtos.responses.UserResponse;
import vn.edu.iuh.authservice.exceptions.impl.EmailExists;
import vn.edu.iuh.authservice.exceptions.impl.UserNotFound;
import vn.edu.iuh.authservice.exceptions.impl.UsernameExists;
import vn.edu.iuh.authservice.mappers.UserMapper;
import vn.edu.iuh.authservice.models.User;
import vn.edu.iuh.authservice.repositories.UserRepository;
import vn.edu.iuh.authservice.services.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @description
 * @author: vie
 * @date: 14/2/25
 */
@Service
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;
   private final UserMapper userMapper;
   private final PasswordEncoder passwordEncoder;

   public UserServiceImpl(UserRepository userRepository,
                          UserMapper userMapper, PasswordEncoder passwordEncoder) {
      this.userRepository = userRepository;
      this.userMapper = userMapper;
      this.passwordEncoder = passwordEncoder;
   }



   @Override
   public UserResponse getUserById(UUID id) {
      return userRepository.findById(id).map(userMapper::toDto).orElseThrow(UserNotFound::new);
   }

   @Override
   public UserResponse getUserByUsername(String username) {
      return userRepository.findByUsernameLikeIgnoreCase(username).map(userMapper::toDto)
            .orElseThrow(UserNotFound::new);
   }

   @Override
   public UserResponse getUserByPhone(String phone) {
      return userRepository.findByPhone(phone).map(userMapper::toDto)
            .orElseThrow(UserNotFound::new);
   }

   @Override
   public UserResponse getUserByEmail(String email) {
      return userRepository.findByEmail(email).map(userMapper::toDto)
            .orElseThrow(UserNotFound::new);
   }

//   @Override
//   public UserResponse updateUser(User user) {
//      if (userRepository.existsById(user.getId())) {
//         return userMapper.toDto(userRepository.save(user));
//      }
//      throw new UserNotFound();
//   }

   @Override
   public UserResponse updateUser(UUID id, UpdateUserRequest userRequest) {
      // Tìm user theo id
      User existingUser = userRepository.findById(id)
              .orElseThrow(UserNotFound::new);

      // Kiểm tra trùng username (nếu username thay đổi)
      if (userRequest.username() != null && !userRequest.username().equals(existingUser.getUsername())) {
         if (userRepository.existsByUsername(userRequest.username())) {
            throw new UsernameExists();
         }
      }

      // Kiểm tra trùng email (nếu email thay đổi)
      if (userRequest.email() != null && !userRequest.email().equals(existingUser.getEmail())) {
         if (userRepository.existsByEmail(userRequest.email())) {
            throw new EmailExists();
         }
      }

      // Kiểm tra trùng phone (nếu phone thay đổi)
      if (userRequest.phone() != null && !userRequest.phone().equals(existingUser.getPhone())) {
         if (userRepository.findByPhone(userRequest.phone()).isPresent()) {
            throw new RuntimeException("Phone number already exists");
         }
      }

      // Handle password separately - we need to encode it if present
      String newPassword = userRequest.password();
      boolean hasNewPassword = newPassword != null && !newPassword.isEmpty();

      // Cập nhật các field từ request vào existingUser
      userMapper.updateUserFromRequest(userRequest, existingUser);

      // If there's a new password, encode it before saving
      if (hasNewPassword) {
         existingUser.setPassword(passwordEncoder.encode(newPassword));
      }

      // Set updatedAt
      existingUser.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

      // Lưu user đã cập nhật
      User updatedUser = userRepository.save(existingUser);
      return userMapper.toDto(updatedUser);
   }

   @Override
   public void deleteUser(UUID id) {
      if (userRepository.existsById(id)) {
         userRepository.deleteById(id);
      } else {
         throw new UserNotFound();
      }
   }

   @Override
   public boolean isOwner(String username, UUID userId) {
      return userRepository.findByUsernameLikeIgnoreCase(username).stream().anyMatch(user -> user.getId().equals(userId));
   }


   @Override
   public List<UserResponse> getAllUser() {
      return StreamSupport.stream(userRepository.findAll().spliterator(), false)
              .map(userMapper::toDto)
              .collect(Collectors.toList());
   }
}
