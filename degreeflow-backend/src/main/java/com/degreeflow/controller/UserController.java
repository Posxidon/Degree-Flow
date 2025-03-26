package com.degreeflow.controller;

import com.degreeflow.model.UserEntity;
import com.degreeflow.repository.UserRepository;
import com.degreeflow.service.UserService; // Inject UserService to handle password hashing
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;  // Injecting UserService

    // Constructor-based injection for UserRepository and UserService
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // Endpoint to get a user by username
    @GetMapping("/{username}")
    public ResponseEntity<UserEntity> getUserByUsername(@PathVariable String username) {
        UserEntity user = userRepository.findById(username).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(404).body(null);
    }

    // Endpoint to update a user, with password hashing if password is changed
    @PutMapping("/{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username, @RequestBody UserEntity userEntity) {
        if (!userRepository.existsById(username)) {
            return ResponseEntity.status(404).body("User not found");
        }

        if (userEntity.getPassword() != null && !userEntity.getPassword().isEmpty()) {
            String hashedPassword = userService.hashPassword(userEntity.getPassword());
            userEntity.setPassword(hashedPassword);
        }

        userEntity.setUsername(username);
        userRepository.save(userEntity);

        return ResponseEntity.ok("User updated successfully");
    }

    // Endpoint to delete a user by username
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        if (!userRepository.existsById(username)) {
            return ResponseEntity.status(404).body("User not found");
        }

        userRepository.deleteById(username);
        return ResponseEntity.ok("User deleted successfully");
    }

    // Endpoint to register a regular user (with USER role)
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserEntity userEntity) {
        if (userRepository.findByUsername(userEntity.getUsername()) != null) {
            return ResponseEntity.status(400).body("Username already exists");
        }

        // Register the user with USER role
        userService.registerUser(userEntity.getUsername(), userEntity.getPassword());
        return ResponseEntity.status(201).body("User registered successfully");
    }

    // Endpoint to register an admin (with ADMIN role)
    @PostMapping("/registerAdmin")
    public ResponseEntity<String> registerAdmin(@RequestBody UserEntity userEntity) {
        if (userRepository.findByUsername(userEntity.getUsername()) != null) {
            return ResponseEntity.status(400).body("Username already exists");
        }

        // Register the user as an admin
        userService.registerAdmin(userEntity.getUsername(), userEntity.getPassword());
        return ResponseEntity.status(201).body("Admin registered successfully");
    }
}
