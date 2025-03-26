package com.degreeflow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_roles")
@Getter  // Lombok will generate getter methods
@Setter  // Lombok will generate setter methods
public class UserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "role_id")
    private Long roleId;

    // Default no-argument constructor
    public UserRoles() {}

    // Constructor with parameters
    public UserRoles(String username, Long roleId) {
        this.username = username;
        this.roleId = roleId;
    }
}
