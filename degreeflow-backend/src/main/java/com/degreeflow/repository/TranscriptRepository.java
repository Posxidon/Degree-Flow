package com.degreeflow.repository;

import com.degreeflow.model.TranscriptData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranscriptRepository extends JpaRepository<TranscriptData, Long> {
    // Additional query methods if needed (e.g., find by program or term)
}
