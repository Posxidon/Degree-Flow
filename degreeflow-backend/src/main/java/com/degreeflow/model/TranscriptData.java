package com.degreeflow.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String program;
    private String plan;

    @Column(nullable = false)
    private double gpa;

    @Column(nullable = false)
    private double totalPoints;

    @Column(nullable = false)
    private double gpaUnits;

    private String termEnrollment;

    @Column(nullable = false)
    private boolean isRecent; // True if within 2 months

    @OneToMany(mappedBy = "transcript", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourseRecord> courses;
}
