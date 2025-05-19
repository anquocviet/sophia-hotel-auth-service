package vn.edu.iuh.authservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.authservice.dtos.requests.ChangePasswordRequest;
import vn.edu.iuh.authservice.dtos.requests.UpdateUserRequest;
import vn.edu.iuh.authservice.dtos.responses.UserResponse;
import vn.edu.iuh.authservice.exceptions.impl.EmailExistsException;
import vn.edu.iuh.authservice.exceptions.impl.InvalidPasswordException;
import vn.edu.iuh.authservice.exceptions.impl.PhoneExistsException;
import vn.edu.iuh.authservice.exceptions.impl.UserNotFoundException;
import vn.edu.iuh.authservice.exceptions.impl.UsernameExistsException;
import vn.edu.iuh.authservice.mappers.UserMapper;
import vn.edu.iuh.authservice.models.User;
import vn.edu.iuh.authservice.repositories.UserRepository;
import vn.edu.iuh.authservice.services.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.StreamSupport;

/**
 * @description User management service implementation
 * @author: vie
 * @date: 14/2/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;
   private final UserMapper userMapper;
   private final PasswordEncoder passwordEncoder;

   @Override
   @Transactional(readOnly = true)
   public UserResponse getUserById(UUID id) {
      log.debug("Fetching user by ID: {}", id);
      return userRepository.findById(id)
            .map(userMapper::toDto)
            .orElseThrow(() -> {
               log.warn("User not found with ID: {}", id);
               return new UserNotFoundException();
            });
   }

   @Override
   @Transactional(readOnly = true)
   public UserResponse getUserByUsername(String username) {
      log.debug("Fetching user by username: {}", username);
      return userRepository.findByUsernameLikeIgnoreCase(username)
            .map(userMapper::toDto)
            .orElseThrow(() -> {
               log.warn("User not found with username: {}", username);
               return new UserNotFoundException();
            });
   }

   @Override
   @Transactional(readOnly = true)
   public UserResponse getUserByPhone(String phone) {
      log.debug("Fetching user by phone: {}", phone);
      return userRepository.findByPhone(phone)
            .map(userMapper::toDto)
            .orElseThrow(() -> {
               log.warn("User not found with phone: {}", phone);
               return new UserNotFoundException();
            });
   }

   @Override
   @Transactional(readOnly = true)
   public UserResponse getUserByEmail(String email) {
      log.debug("Fetching user by email: {}", email);
      return userRepository.findByEmail(email)
            .map(userMapper::toDto)
            .orElseThrow(() -> {
               log.warn("User not found with email: {}", email);
               return new UserNotFoundException();
            });
   }

   @Override
   public UserResponse updateUser(UUID id, UpdateUserRequest userRequest) {
      log.info("Updating user with ID: {}", id);
      
      // Find user by ID
      User existingUser = findUserById(id);

      // Validate unique fields
      validateUniqueUsernameForUpdate(userRequest.username(), existingUser);
      validateUniqueEmailForUpdate(userRequest.email(), existingUser);
      validateUniquePhoneForUpdate(userRequest.phone(), existingUser);

      // Update user from request
      updateUserFields(userRequest, existingUser);
      
      // Save updated user
      User updatedUser = userRepository.save(existingUser);
      log.info("User updated successfully with ID: {}", id);
      
      return userMapper.toDto(updatedUser);
   }

   @Override
   public void deleteUser(UUID id) {
      log.info("Soft deleting user with ID: {}", id);

      User user = userRepository.findById(id)
              .orElseThrow(() -> {
                 log.warn("Cannot delete - User not found with ID: {}", id);
                 return new UserNotFoundException();
              });

      // Set deleted timestamp
      user.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
      userRepository.save(user);

      log.info("User soft deleted successfully with ID: {}", id);
   }

   @Override
   @Transactional(readOnly = true)
   public boolean isOwner(String username, UUID userId) {
      log.debug("Checking if user {} is owner of resource with ID: {}", username, userId);
      
      return userRepository.findByUsernameLikeIgnoreCase(username)
            .filter(user -> Objects.equals(user.getId(), userId))
            .isPresent();
   }

   @Override
   @Transactional(readOnly = true)
   public List<UserResponse> getAllUser() {
      log.debug("Fetching all users");
      
      return StreamSupport.stream(userRepository.findAll().spliterator(), false)
            .map(userMapper::toDto)
            .toList();
   }

   @Override
   public void changePassword(ChangePasswordRequest request) {
      log.info("Processing password change for user ID: {}", request.userId());
      
      User user = findUserById(request.userId());
      
      // Validate old password
      if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
         log.warn("Password change failed - Invalid old password for user ID: {}", request.userId());
         throw new InvalidPasswordException();
      }
      
      // Update password
      user.setPassword(passwordEncoder.encode(request.newPassword()));
      user.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
      userRepository.save(user);
      
      log.info("Password changed successfully for user ID: {}", request.userId());
   }
   
   /**
    * Find a user by ID or throw UserNotFoundException
    */
   private User findUserById(UUID id) {
      return userRepository.findById(id)
            .orElseThrow(() -> {
               log.warn("User not found with ID: {}", id);
               return new UserNotFoundException();
            });
   }
   
   /**
    * Validate that the username is unique for the update operation
    */
   private void validateUniqueUsernameForUpdate(String newUsername, User existingUser) {
      if (newUsername != null && !newUsername.equals(existingUser.getUsername()) && 
            userRepository.existsByUsername(newUsername)) {
         log.warn("Update failed - Username already exists: {}", newUsername);
         throw new UsernameExistsException();
      }
   }
   
   /**
    * Validate that the email is unique for the update operation
    */
   private void validateUniqueEmailForUpdate(String newEmail, User existingUser) {
      if (newEmail != null && !newEmail.equals(existingUser.getEmail()) && 
            userRepository.existsByEmail(newEmail)) {
         log.warn("Update failed - Email already exists: {}", newEmail);
         throw new EmailExistsException();
      }
   }
   
   /**
    * Validate that the phone is unique for update operation
    */
   private void validateUniquePhoneForUpdate(String newPhone, User existingUser) {
      if (newPhone != null && !newPhone.equals(existingUser.getPhone()) && 
            userRepository.findByPhone(newPhone).isPresent()) {
         log.warn("Update failed - Phone already exists: {}", newPhone);
         throw new PhoneExistsException();
      }
   }
   
   /**
    * Update user fields from request
    */
   private void updateUserFields(UpdateUserRequest userRequest, User existingUser) {
      // Update basic fields
      userMapper.updateUserFromRequest(userRequest, existingUser);
      
      // Handle password separately if provided
      String newPassword = userRequest.password();
      if (newPassword != null && !newPassword.isEmpty()) {
         existingUser.setPassword(passwordEncoder.encode(newPassword));
      }
      
      // Set updated timestamp
      existingUser.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
   }
}
