package com.degreeflow.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public boolean isMinor;
    private String degreeName;
    @ManyToOne
    @JoinColumn(name = "required_courses_id")
    private DegreePlan degreePlan;

    public DegreePlan GetRequiredCourses(){
        return this.degreePlan;
    }
    // No-argument constructor for testing
    public Degree() {
    }

    // Constructor with parameters
    public Degree(String degreeName, DegreePlan degreePlan, boolean isMinor) {
        this.degreeName = degreeName;
        this.degreePlan = degreePlan;
        this.isMinor = isMinor;
    }
}
