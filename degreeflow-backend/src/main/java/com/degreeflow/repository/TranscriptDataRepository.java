package com.degreeflow.repository;

import com.degreeflow.model.TranscriptData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TranscriptDataRepository extends JpaRepository<TranscriptData, Long> {
    Optional<TranscriptData> findByTranscriptId(String transcriptId);
}
