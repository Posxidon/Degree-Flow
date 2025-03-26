package com.degreeflow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private String username;

    private String password;

    // Many-to-many relationship with Role entity
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles", // join table between users and roles
            joinColumns = @JoinColumn(name = "username"), // user reference
            inverseJoinColumns = @JoinColumn(name = "role_id") // role reference
    )
    private Set<Role> roles; // This will hold the roles associated with the user
}
