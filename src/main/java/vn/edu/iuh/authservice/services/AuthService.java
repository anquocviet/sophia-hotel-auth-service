package vn.edu.iuh.authservice.services;

import org.springframework.stereotype.Service;
import vn.edu.iuh.authservice.dtos.requests.CreateUserRequest;
import vn.edu.iuh.authservice.dtos.requests.LoginRequest;
import vn.edu.iuh.authservice.dtos.responses.LoginResponse;

@Service
public interface AuthService {
    LoginResponse login(LoginRequest signInRequest);
    String register(CreateUserRequest signUpRequest);
}