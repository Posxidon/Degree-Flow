package com.degreeflow.service;

import com.degreeflow.model.UserEntity;
import com.degreeflow.repository.UserRepository;
import com.degreeflow.repository.RoleRepository;
import com.degreeflow.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // Hash password before saving
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // Register a user with the "USER" role
    public void registerUser(String username, String rawPassword) {
        String hashedPassword = hashPassword(rawPassword);
        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);

        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = new Role("USER");
            roleRepository.save(userRole);
        }
        newUser.setRoles(Set.of(userRole));

        userRepository.save(newUser);
    }

    // Register an admin with the "ADMIN" role
    public void registerAdmin(String username, String rawPassword) {
        String hashedPassword = hashPassword(rawPassword);
        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);

        Role adminRole = roleRepository.findByName("ADMIN");
        if (adminRole == null) {
            adminRole = new Role("ADMIN");
            roleRepository.save(adminRole);
        }
        newUser.setRoles(Set.of(adminRole));

        userRepository.save(newUser);
    }

    // Validate user login by checking if username exists and password matches
    public boolean validateUserLogin(String username, String rawPassword) {
        UserEntity user = userRepository.findByUsername(username);
        return user != null && passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
