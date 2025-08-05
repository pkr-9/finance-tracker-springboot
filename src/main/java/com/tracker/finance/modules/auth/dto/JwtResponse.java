package com.tracker.finance.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {

    private String token;
    private final String type = "Bearer";
    private UserInfo user;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserInfo {
        private String id;
        private String username;
        private String email;
    }
}