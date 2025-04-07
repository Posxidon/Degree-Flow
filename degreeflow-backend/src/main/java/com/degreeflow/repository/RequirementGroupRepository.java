package com.degreeflow.repository;

import com.degreeflow.model.DataRequirementGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RequirementGroupRepository extends JpaRepository<DataRequirementGroup, Integer> {

    /**
     * Finds all requirement groups associated with a specific transcript ID.
     *
     * @param transcriptId the ID of the transcript
     * @return list of matching requirement groups
     */
    List<DataRequirementGroup> findByTranscriptId(String transcriptId);


    /**
     * Checks to see if there are requirement groups associated with a specific transcript ID.
     *
     * @param transcriptId the ID of the transcript
     * @return true if exist, false else wise.
     */
    boolean existsByTranscriptId(String transcriptId);
}
