package com.tracker.finance.modules.auth.service.impl;

import com.tracker.finance.core.security.jwt.JwtProvider;
import com.tracker.finance.modules.auth.dto.LoginRequest;
import com.tracker.finance.modules.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // Spring will automatically inject the AuthenticationManager bean.
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public String login(LoginRequest request) {
        // This block will now work correctly because authenticationManager is no longer
        // null.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }
}
