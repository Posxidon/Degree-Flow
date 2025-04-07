package com.degreeflow.repository;

import com.degreeflow.model.TranscriptData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TranscriptDataRepository extends JpaRepository<TranscriptData, Long> {


    Optional<TranscriptData> findByTranscriptId(String transcriptId);

    // Get unprocessed (unchecked) and passed courses for a specific transcript
    // give transcript ID and also a list of grades you want excluded from the returned list.
    List<TranscriptData> findByTranscriptIdAndCheckValueFalseAndGradeNotIn(String transcriptId, List<String> excludedGrades);

    // Optional: Get one course by code and transcript to update it
    TranscriptData findByTranscriptIdAndCourseCode(String transcriptId, String courseCode);

    List<TranscriptData> findAllByTranscriptId(String transcriptId);
}
