package com.degreeflow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "course_record")
@Data
public class CourseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_code")
    private String courseCode;

    private String title;

    private String term;

    private Double units;

    private String grade;

    @Column(name = "transcript_id")
    private String transcriptId;

    private Boolean checked = false; // Optional: useful for tracking processed courses

    // No-argument constructor
    public CourseRecord() {
    }

    // Constructor with parameters
    public CourseRecord(String courseCode, String title, String term, Double units, String grade, String transcriptId) {
        this.courseCode = courseCode;
        this.title = title;
        this.term = term;
        this.units = units;
        this.grade = grade;
        this.transcriptId = transcriptId;
        this.checked = false;
    }

    public String getCourseCode() {
        return this.courseCode;
    }
    
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}