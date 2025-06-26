package com.tracker.finance.modules.user.service.impl;

import com.tracker.finance.core.exception.specific_exceptions.BadRequestException;
import com.tracker.finance.core.exception.specific_exceptions.ResourceNotFoundException;
import com.tracker.finance.modules.auth.dto.RegisterRequest;
import com.tracker.finance.modules.user.User;
import com.tracker.finance.modules.user.UserRepository;
import com.tracker.finance.modules.user.dto.UpdateProfileRequest;
import com.tracker.finance.modules.user.dto.UserProfileDto;
import com.tracker.finance.modules.user.mapper.UserMapper;
import com.tracker.finance.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getCurrentUser(String username) {
        User user = findUserByUsername(username);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already in use.");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserProfileDto updateProfile(String currentUsername, UpdateProfileRequest request) {
        User user = findUserByUsername(currentUsername);

        // Update username if provided
        if (StringUtils.hasText(request.getUsername()) && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new BadRequestException("New username is already taken.");
            }
            user.setUsername(request.getUsername());
        }

        // Update password if provided
        if (StringUtils.hasText(request.getNewPassword())) {
            if (!StringUtils.hasText(request.getCurrentPassword())) {
                throw new BadRequestException("Current password is required to set a new password.");
            }
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new BadRequestException("Invalid current password.");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteAccount(String username) {
        User user = findUserByUsername(username);
        userRepository.delete(user);
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
}
