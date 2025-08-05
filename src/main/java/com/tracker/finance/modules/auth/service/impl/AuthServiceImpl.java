package com.tracker.finance.modules.auth.service.impl;

import com.tracker.finance.core.security.jwt.JwtProvider;
import com.tracker.finance.modules.auth.dto.LoginRequest;
import com.tracker.finance.modules.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    // Define the final fields for the dependencies
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    /**
     * Explicit constructor for dependency injection.
     * This replaces the @RequiredArgsConstructor annotation to make the dependency
     * requirement clearer to the Spring Framework, which can resolve startup
     * issues.
     *
     * @param authenticationManager The AuthenticationManager bean provided by
     *                              Spring Security.
     * @param jwtProvider           The custom JwtProvider for creating tokens.
     */
    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public String login(LoginRequest request) {
        // This block will now work correctly as Spring is explicitly told
        // how to construct this service and provide its dependencies.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }
}
