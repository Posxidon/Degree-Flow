package com.degreeflow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

@Entity
@Table(name = "json_schedule")
public class JsonSchedule {

    // This field is mapped by default to "user_id" because of naming conventions.
    private Long userId;

    // If you also need a reference to the User entity:
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    // ... other properties, getters, and setters ...
}

