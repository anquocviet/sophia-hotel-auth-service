package vn.edu.iuh.authservice.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.iuh.authservice.dtos.requests.CreateUserRequest;
import vn.edu.iuh.authservice.dtos.requests.LoginRequest;
import vn.edu.iuh.authservice.dtos.responses.LoginResponse;
import vn.edu.iuh.authservice.mappers.UserMapper;
import vn.edu.iuh.authservice.models.User;
import vn.edu.iuh.authservice.repositories.UserRepository;
import vn.edu.iuh.authservice.services.AuthService;
import vn.edu.iuh.authservice.utils.JWTUtil;

/**
 * @description
 * @author: vie
 * @date: 26/2/25
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
   private final AuthenticationManager authenticationManager;
   private final JWTUtil jwtUtil;
   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
   private final UserMapper userMapper;

   public AuthServiceImpl(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
      this.authenticationManager = authenticationManager;
      this.jwtUtil = jwtUtil;
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
      this.userMapper = userMapper;
   }

   public LoginResponse login(LoginRequest request) {
      Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);

      String token = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());
      return new LoginResponse(token);
   }

   public String register(CreateUserRequest registerRequest) {
      if (userRepository.findByUsernameLikeIgnoreCase(registerRequest.username()).isPresent()) {
         throw new IllegalArgumentException("User with username '" + registerRequest.username() + "' already exists.");
      }

      User newUser = userMapper.toEntity(registerRequest);
      newUser.setPassword(passwordEncoder.encode(registerRequest.password()));

      userRepository.save(newUser);
      return "User registered successfully!";
   }

}
