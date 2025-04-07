package com.degreeflow.service;


import com.degreeflow.model.TranscriptData;
import com.degreeflow.model.DataRequirementGroup;


import com.degreeflow.repository.TranscriptDataRepository;
import com.degreeflow.repository.RequirementGroupRepository;
import com.degreeflow.service.RequirementGroupService;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProgressTracker {

  private final TranscriptDataRepository transcriptDataRepository;
  private final RequirementGroupService requirementGroupService;
  private final RequirementGroupRepository requirementGroupRepository;

  public ProgressTracker(
      TranscriptDataRepository transcriptDataRepository,
      RequirementGroupService requirementGroupService,
      RequirementGroupRepository requirementGroupRepository
  ) {
    this.transcriptDataRepository = transcriptDataRepository;
    this.requirementGroupService = requirementGroupService;
    this.requirementGroupRepository = requirementGroupRepository;
  }

  /**
   * Updates the degree progress tracker with updated transcript information
   * 
   * @param transcriptId the student’s transcript ID
   * @return nothing, just updates database
   */
  public void updateTranscript(String transcriptId) {
    String program = getProgramFromTranscript(transcriptId);

    if (program == null) {
        throw new IllegalArgumentException("No program found for transcript ID: " + transcriptId);
    }

    inputRequirementsToDatabase(transcriptId);

    // Fetch degree requirements and associate them with the transcript
    requirementGroupService.fetchFromApiAndSave(program, transcriptId);

    // Get all student courses that have not been checked and are a passing grade
    List<String> excludedGrades = List.of("W", "F");
    List<TranscriptData> validCourses = transcriptDataRepository
            .findByTranscriptIdAndCheckValueFalseAndGradeNotIn(transcriptId, excludedGrades);

    // Get all the requirement Groups associated with a transcript ID
    List<DataRequirementGroup> requirementGroups = requirementGroupRepository
            .findByTranscriptId(transcriptId);

    for (DataRequirementGroup group : requirementGroups) {
      List<String> courseCodes = group.getCourses(); // Courses in this requirement group
    
      for (TranscriptData course : validCourses) {
        String achievedCodeSpaced = course.getCourseCode();
        String achievedCode = achievedCodeSpaced.replace(" ", "");
    
        if (courseCodes.contains(achievedCode)) {
          group.setNumCompleted(group.getNumCompleted() + 1);
          course.setCheckValue(true);
    
          // Save changes to the course (mark as checked)
          transcriptDataRepository.save(course);
        }
      }  

    }
    // You can now add other logic here:
    // - calculate progress
    // - generate alerts
    // - update derived data
    System.out.println("Transcript " + transcriptId + " successfully updated with program: " + program);
  }

  /**
   * Fetches the requirments associated with a given transcript ID. And adds them to the database
   * 
   * @param transcriptId the student’s transcript ID
   * @return the requirment json data, or null if not found
   */
  public void inputRequirementsToDatabase(String transcriptId)
  {
    String program = getProgramFromTranscript(transcriptId);

    if (program == null) {
      throw new IllegalArgumentException("No program found for transcript ID: " + transcriptId);
    }

    System.out.println("Fetching degree requirements for program: " + program);
    requirementGroupService.fetchFromApiAndSave(program, transcriptId);
  }

  /**
   * Fetches the program associated with a given transcript ID.
   * 
   * @param transcriptId the student’s transcript ID
   * @return the program name, or null if not found
   */
  public String getProgramFromTranscript(String transcriptId) 
  {
    return transcriptDataRepository.findByTranscriptId(transcriptId)
      .map(TranscriptData::getProgram)
      .orElse(null);
  }
}
