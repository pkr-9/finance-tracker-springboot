package com.tracker.finance.modules.user.mapper;

import com.tracker.finance.modules.user.User;
import com.tracker.finance.modules.user.dto.UserProfileDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserProfileDto toDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}