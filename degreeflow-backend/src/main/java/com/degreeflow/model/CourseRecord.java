package com.degreeflow.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String courseCode;

    private String title;

    private String term;

    @Column(nullable = false)
    private double units;

    private String grade;

    @ManyToOne
    @JoinColumn(name = "transcript_id", referencedColumnName = "id", nullable = false)
    private TranscriptData transcript;
}
