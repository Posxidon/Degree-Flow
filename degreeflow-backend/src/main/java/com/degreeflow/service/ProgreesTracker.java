package com.degreeflow.service;

import com.degreeflow.model.TranscriptData;
import com.degreeflow.repository.TranscriptDataRepository;
import org.springframework.stereotype.Service;

@Service
public class ProgressTracker {

    private final TranscriptDataRepository transcriptRepo;

    public ProgressTracker(TranscriptDataRepository transcriptRepo) {
        this.transcriptRepo = transcriptRepo;
    }

    /**
     * Fetches the program associated with a given transcript ID.
     * 
     * @param transcriptId the student’s transcript ID
     * @return the program name, or null if not found
     */
    public String getProgramFromTranscript(long transcriptId) {
        return transcriptRepo.findByTranscriptId(transcriptId)
                .map(TranscriptData::getProgram)
                .orElse(null);
    }

    public void fetchRequirementsByProgram(long transcriptId) {
      String program = getProgramFromTranscript(transcriptId);
  
      if (program == null) {
          throw new IllegalArgumentException("No program found for transcript ID: " + transcriptId);
      }
  
      System.out.println("Fetching degree requirements for program: " + program);
      requirementGroupService.fetchFromApiAndSave(program);
  }
    // You can add more logic here later for:
    // - course progress
    // - requirement checks
    // - alerts etc.
}