package com.tracker.finance.modules.auth.dto;

// import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// @AllArgsConstructor
public class JwtResponse {

    private String token;
    private final String type = "Bearer";

    public JwtResponse(String accessToken) {
        this.token = accessToken;
    }
}