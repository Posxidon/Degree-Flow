package com.degreeflow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(
        name = "Authentication",
        description = "Endpoints related to authentication and JWT"
)
@RequestMapping({"/api"})
public class AuthController {
    public AuthController() {
    }

    @Operation(
            summary = "Public endpoint",
            description = "Accessible without authentication."
    )
    @GetMapping({"/public"})
    public Map<String, String> publicEndpoint() {
        return Map.of("message", "This is a public endpoint");
    }

    @Operation(
            summary = "Protected endpoint",
            description = "Requires a valid JWT token."
    )
    @GetMapping({"/protected"})
    public Map<String, Object> protectedEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return Map.of("message", "This is a protected endpoint", "user", jwt.getClaims());
    }
}
