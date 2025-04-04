package com.degreeflow.repository;

import com.degreeflow.model.CourseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRecordRepository extends JpaRepository<CourseRecord, Long> {

    // Get all courses by transcript ID
    List<CourseRecord> findByTranscriptId(String transcriptId);

    // Get unprocessed (unchecked) and passed courses for a specific transcript
    // give transcript ID and also a list of grades you want excluded from the returned list.
    List<CourseRecord> findByTranscriptIdAndCheckedFalseAndGradeNotIn(String transcriptId, List<String> excludedGrades);

    // Optional: Get one course by code and transcript to update it
    CourseRecord findByTranscriptIdAndCourseCode(String transcriptId, String courseCode);
}
