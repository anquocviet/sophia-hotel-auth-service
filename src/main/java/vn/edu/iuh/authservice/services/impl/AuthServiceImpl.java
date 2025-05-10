package vn.edu.iuh.authservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.authservice.dtos.requests.CreateUserRequest;
import vn.edu.iuh.authservice.dtos.requests.LoginRequest;
import vn.edu.iuh.authservice.dtos.responses.LoginResponse;
import vn.edu.iuh.authservice.exceptions.impl.EmailExistsException;
import vn.edu.iuh.authservice.exceptions.impl.InvalidCredentialsException;
import vn.edu.iuh.authservice.exceptions.impl.PhoneExistsException;
import vn.edu.iuh.authservice.exceptions.impl.UserNotFoundException;
import vn.edu.iuh.authservice.exceptions.impl.UsernameExistsException;
import vn.edu.iuh.authservice.mappers.UserMapper;
import vn.edu.iuh.authservice.models.User;
import vn.edu.iuh.authservice.repositories.UserRepository;
import vn.edu.iuh.authservice.services.AuthService;
import vn.edu.iuh.authservice.utils.JWTUtil;

/**
 * @description Authentication service for user login and registration
 * @author: vie
 * @date: 26/2/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
   private final AuthenticationManager authenticationManager;
   private final JWTUtil jwtUtil;
   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
   private final UserMapper userMapper;

   @Override
   public LoginResponse login(LoginRequest request) {
      try {
         log.info("Attempting login for user: {}", request.username());
         
         // Authenticate the user credentials
         Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(request.username(), request.password())
         );

         // Set authentication in security context
         SecurityContextHolder.getContext().setAuthentication(authentication);

         // Get user details and generate token
         UserDetails userDetails = (UserDetails) authentication.getPrincipal();
         User user = userRepository.findByUsername(userDetails.getUsername())
               .orElseThrow(UserNotFoundException::new);
      
         String token = JWTUtil.generateToken(userDetails);
         
         log.info("User logged in successfully: {}", request.username());
         return new LoginResponse(token, user.getId(), user.getRole());
      } catch (BadCredentialsException e) {
         log.error("Login failed - Invalid credentials for user: {}", request.username());
         throw new InvalidCredentialsException();
      } catch (Exception e) {
         log.error("Login failed with unexpected error for user: {}", request.username(), e);
         throw e;
      }
   }

   @Override
   public String register(CreateUserRequest registerRequest) {
      log.info("Processing registration request for username: {}", registerRequest.username());
      
      // Validate unique constraints
      validateUniqueUsername(registerRequest.username());
      validateUniqueEmail(registerRequest.email());

      // Create and save the new user
      User newUser = createUser(registerRequest);
      userRepository.save(newUser);
      
      log.info("User registered successfully: {}", registerRequest.username());
      return "User registered successfully!";
   }
   
   /**
    * Validates that the username is unique
    */
   private void validateUniqueUsername(String username) {
      if (userRepository.existsByUsername(username)) {
         log.warn("Registration failed: Username already exists - {}", username);
         throw new UsernameExistsException();
      }
   }
   
   /**
    * Validates that the email is unique if provided
    */
   private void validateUniqueEmail(String email) {
      if (email != null && !email.isBlank() && userRepository.existsByEmail(email)) {
         log.warn("Registration failed: Email already exists - {}", email);
         throw new EmailExistsException();
      }
   }
   
   /**
    * Validates that the phone is unique if provided
    */
   private void validateUniquePhone(String phone) {
      if (phone != null && !phone.isBlank() && userRepository.findByPhone(phone).isPresent()) {
         log.warn("Registration failed: Phone already exists - {}", phone);
         throw new PhoneExistsException();
      }
   }
   
   /**
    * Creates a new User entity from the registration request
    */
   private User createUser(CreateUserRequest registerRequest) {
      User newUser = userMapper.toEntity(registerRequest);
      newUser.setPassword(passwordEncoder.encode(registerRequest.password()));
      return newUser;
   }
}
