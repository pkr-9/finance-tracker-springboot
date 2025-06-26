package com.tracker.finance.modules.auth.controller;

// import com.tracker.finance.modules.user.dto.UpdateProfileRequest;
// import com.tracker.finance.modules.user.dto.UserProfileDto;
// import org.springframework.http.HttpStatus;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails;
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
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    // @GetMapping("/me")
    // public ResponseEntity<UserProfileDto> getCurrentUser(@AuthenticationPrincipal
    // UserDetails userDetails) {
    // UserProfileDto userProfile =
    // authService.getCurrentUser(userDetails.getUsername());
    // return ResponseEntity.ok(userProfile);
    // }

    // @PutMapping("/profile")
    // public ResponseEntity<UserProfileDto> updateProfile(@AuthenticationPrincipal
    // UserDetails userDetails,
    // @Valid @RequestBody UpdateProfileRequest request) {
    // UserProfileDto updatedUser =
    // authService.updateProfile(userDetails.getUsername(), request);
    // return ResponseEntity.ok(updatedUser);
    // }

    // @DeleteMapping("/profile")
    // public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal UserDetails
    // userDetails) {
    // authService.deleteAccount(userDetails.getUsername());
    // return ResponseEntity.noContent().build();
    // }
}
