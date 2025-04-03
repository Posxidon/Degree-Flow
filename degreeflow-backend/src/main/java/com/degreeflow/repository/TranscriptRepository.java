package com.degreeflow.repository;

import com.degreeflow.model.TranscriptData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TranscriptRepository extends JpaRepository<TranscriptData, Long> {
    Optional<TranscriptData> findByStudentId(String studentId);
    Optional<TranscriptData> findByStudentIdAndTerm(String studentId, String term);
    List<TranscriptData> findAllByStudentId(String studentId);
}
