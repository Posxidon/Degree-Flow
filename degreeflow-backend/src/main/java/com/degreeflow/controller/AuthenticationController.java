package com.degreeflow.controller;

import com.degreeflow.model.JwtResponse;
import com.degreeflow.model.UserEntity;
import com.degreeflow.model.Role;
import com.degreeflow.model.UserRoles;
import com.degreeflow.repository.UserRepository;
import com.degreeflow.repository.RoleRepository;
import com.degreeflow.repository.UserRolesRepository;
import com.degreeflow.security.JwtUtil;
import com.degreeflow.security.CustomUserDetailsService;
import com.degreeflow.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final RoleRepository roleRepository;
    private final UserRolesRepository userRolesRepository;
    private final UserService userService;

    public AuthenticationController(JwtUtil jwtUtil,
                                    UserRepository userRepository,
                                    PasswordEncoder passwordEncoder,
                                    CustomUserDetailsService userDetailsService,
                                    RoleRepository roleRepository,
                                    UserRolesRepository userRolesRepository,
                                    UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.roleRepository = roleRepository;
        this.userRolesRepository = userRolesRepository;
        this.userService = userService;
    }

    // Register a user with the "USER" role
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserEntity userEntity) {
        if (userRepository.findByUsername(userEntity.getUsername()) != null) {
            return ResponseEntity.status(400).body("Username already exists");
        }

        // Hash password before saving
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        // Check if "USER" role exists, if not, create it
        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = new Role("USER");
            roleRepository.save(userRole);
        }

        // Assign the role to the user
        userEntity.setRoles(Set.of(userRole));
        userRepository.save(userEntity);
        userRolesRepository.save(new UserRoles(userEntity.getUsername(), userRole.getId()));

        return ResponseEntity.status(201).body("User registered successfully");
    }

    // Register an admin with the "ADMIN" role
    @PostMapping("/registerAdmin")
    public ResponseEntity<String> registerAdmin(@RequestBody UserEntity userEntity) {
        if (userRepository.findByUsername(userEntity.getUsername()) != null) {
            return ResponseEntity.status(400).body("Username already exists");
        }

        // Hash password before saving
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        // Check if "ADMIN" role exists, if not, create it
        Role adminRole = roleRepository.findByName("ADMIN");
        if (adminRole == null) {
            adminRole = new Role("ADMIN");
            roleRepository.save(adminRole);
        }

        // Assign the role to the user
        userEntity.setRoles(Set.of(adminRole));
        userRepository.save(userEntity);
        userRolesRepository.save(new UserRoles(userEntity.getUsername(), adminRole.getId()));

        return ResponseEntity.status(201).body("Admin registered successfully");
    }

    // Login endpoint to generate JWT token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserEntity userEntity) {
        boolean isValidUser = userService.validateUserLogin(userEntity.getUsername(), userEntity.getPassword());
        if (!isValidUser) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEntity.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse("Bearer " + token));
    }
}
