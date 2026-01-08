package com.template.controller;

import com.template.dto.ApiResponse;
import com.template.dto.LoginRequest;
import com.template.dto.LoginResponse;
import com.template.entity.User;
import com.template.repository.UserRepository;
import com.template.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            User user = userRepository.findByEmailAndActive(loginRequest.getEmail())
                    .orElseThrow(() -> new AuthenticationException("User not found") {});

            String token = jwtService.generateToken(user);
            LoginResponse loginResponse = LoginResponse.builder()
                    .token(token)
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .role(user.getRole().toString())
                    .build();

            log.info("User logged in successfully: {}", loginRequest.getEmail());
            ApiResponse<?> response = new ApiResponse<>(true, "Login successful", loginResponse);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", loginRequest.getEmail());
            ApiResponse<?> response = new ApiResponse<>(false, "Invalid email or password");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(
            @Valid @RequestBody User user) {
        log.info("Registration attempt for email: {}", user.getEmail());
        
        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("Registration failed: Email already exists: {}", user.getEmail());
            ApiResponse<?> response = new ApiResponse<>(false, "Email already registered");
            return ResponseEntity.badRequest().body(response);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.USER);
        user.setIsActive(true);
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", user.getEmail());
        
        ApiResponse<?> response = new ApiResponse<>(true, "Registration successful");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<?>> validateToken() {
        log.info("Token validation endpoint called");
        ApiResponse<?> response = new ApiResponse<>(true, "Token is valid");
        return ResponseEntity.ok(response);
    }
}
