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

    @Column(nullable = false, unique = true)
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

    public void encryptData() {
        this.program = EncryptionUtil.encrypt(program);
        this.term = EncryptionUtil.encrypt(term);
        this.coursesTaken = EncryptionUtil.encrypt(coursesTaken);
        this.grades = EncryptionUtil.encrypt(grades);
        this.gpa = EncryptionUtil.encrypt(gpa);
        this.totalGpa = EncryptionUtil.encrypt(totalGpa);
        this.units = EncryptionUtil.encrypt(units);
        this.coOp = EncryptionUtil.encrypt(coOp);
    }

    public void decryptData() {
        this.program = EncryptionUtil.decrypt(program);
        this.term = EncryptionUtil.decrypt(term);
        this.coursesTaken = EncryptionUtil.decrypt(coursesTaken);
        this.grades = EncryptionUtil.decrypt(grades);
        this.gpa = EncryptionUtil.decrypt(gpa);
        this.totalGpa = EncryptionUtil.decrypt(totalGpa);
        this.units = EncryptionUtil.decrypt(units);
        this.coOp = EncryptionUtil.decrypt(coOp);
    }
}
