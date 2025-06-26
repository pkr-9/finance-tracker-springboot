package com.tracker.finance.modules.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username; // New username (optional)

    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    private String newPassword; // New password (optional)

    private String currentPassword; // Required if changing password
}