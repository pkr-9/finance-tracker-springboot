package com.tracker.finance.modules.user.service;

import com.tracker.finance.modules.auth.dto.RegisterRequest;
import com.tracker.finance.modules.user.dto.UpdateProfileRequest;
import com.tracker.finance.modules.user.dto.UserProfileDto;

public interface UserService {
    void register(RegisterRequest request);

    UserProfileDto getCurrentUser(String username);

    UserProfileDto updateProfile(String username, UpdateProfileRequest request);

    void deleteAccount(String username);
}