package com.degreeflow.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @GetMapping("/public")
    public Map<String, String> publicEndpoint() {
        return Map.of("message", "This is a public endpoint");
    }

    @GetMapping("/protected")
    public Map<String, Object> protectedEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
                "message", "This is a protected endpoint",
                "user", jwt.getClaims()
        );
    }
}
