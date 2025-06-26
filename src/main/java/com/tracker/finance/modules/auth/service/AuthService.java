package com.tracker.finance.modules.auth.service;

import com.tracker.finance.modules.auth.dto.LoginRequest;

public interface AuthService {
    String login(LoginRequest request);
}