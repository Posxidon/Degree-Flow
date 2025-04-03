package com.degreeflow.model;

import jakarta.persistence.*;
import lombok.*;
import com.degreeflow.util.EncryptionUtil;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transcript_data")
public class TranscriptData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false, length = 1024)
    private String program;

    @Column(nullable = false, length = 100)
    private String term;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String coursesTaken;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String grades;

    @Column(nullable = false, length = 10)
    private String gpa;

    @Column(nullable = false, length = 10)
    private String totalGpa;

    @Column(nullable = false, length = 50)
    private String units;

    @Column(nullable = false, length = 512)
    private String coOp;

    @Column(name = "transcript_id", nullable = false)
    private String transcriptId;



}
