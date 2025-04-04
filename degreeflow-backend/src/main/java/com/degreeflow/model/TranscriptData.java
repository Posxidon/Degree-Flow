package com.degreeflow.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transcript_data")
public class TranscriptData {

    // === Primary Key ===
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // === Student + Transcript Metadata ===
    @Column(nullable = false)
    private String studentId;

    @Column(name = "transcript_id", nullable = false, length = 100)
    private String transcriptId;

    @Column(nullable = false, length = 100)
    private String term;

    @Column(nullable = false, length = 1024)
    private String program;

    @Column(nullable = false)
    private String coOp;

    // === Term-level GPA Info ===
    @Column(nullable = false, length = 10)
    private String units;

    @Column(nullable = false, length = 10)
    private String gpa;

    @Column(nullable = false, length = 10)
    private String totalGpa;

    // === Per-Course Fields (if applicable) ===
    @Column(length = 100)
    private String courseCode;

    @Column(length = 1024)
    private String courseTitle;

    @Column(length = 10)
    private String grade;

    // === Raw Extraction Backups (Optional) ===
    @Column(columnDefinition = "TEXT")
    private String coursesTaken;

    @Column(columnDefinition = "TEXT")
    private String grades;

    // === Admin / Manual Review Support ===
    @Column(nullable = false)
    private boolean checkValue = false;

    public String getProgram() {
        return program;
    }

    public String getCourseCode() {
        return this.courseCode;
    }
    
    public void setCheckValue(boolean checked) {
        this.checkValue = checked;
    }

    public String getGrade() {
        return this.grade;
    }
}