package com.tracker.finance.modules.auth.controller;

import com.tracker.finance.modules.user.dto.UserProfileDto;
import com.tracker.finance.modules.user.service.UserService;
import com.tracker.finance.modules.auth.dto.JwtResponse;
import com.tracker.finance.modules.auth.dto.LoginRequest;
import com.tracker.finance.modules.auth.dto.RegisterRequest;
import com.tracker.finance.modules.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String jwt = authService.login(loginRequest);

        // Fetch user details after successful login
        UserProfileDto userProfile = userService.getCurrentUser(loginRequest.getUsername());

        // Create the user info object for the response
        JwtResponse.UserInfo userInfo = new JwtResponse.UserInfo(
                userProfile.getId().toString(),
                userProfile.getUsername(),
                userProfile.getEmail());

        // Return the new response object
        return ResponseEntity.ok(new JwtResponse(jwt, userInfo));
    }
}
