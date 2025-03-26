package com.degreeflow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_ratings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email", "courseCode"}))
@Data
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String courseCode;
    private Integer stars;
    private LocalDateTime submittedAt;

    // No-argument constructor
    public Rating() {
    }

    // Constructor with parameters
    public Rating(String email, String courseCode, Integer stars) {
        this.email = email;
        this.courseCode = courseCode;
        this.stars = stars;
        this.submittedAt = LocalDateTime.now();
    }
}