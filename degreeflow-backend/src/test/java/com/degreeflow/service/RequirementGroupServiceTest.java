package com.degreeflow.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.degreeflow.repository.RequirementGroupRepository;
import com.degreeflow.model.DataRequirementGroup;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;


@SpringBootTest
@Transactional
public class RequirementGroupServiceTest {

  @Autowired
  private RequirementGroupService requirementGroupService;

  @Autowired
  private RequirementGroupRepository requirementGroupRepository;

  /**
   * Tests the saveFromRawData method to ensure that raw requirement group data
   * from the API is correctly parsed and saved into the database.
   *
   * This test simulates an API response with one requirement group containing:
   * - 5 courses
   * - a null name (which should default the group's type to "Required")
   *
   * It verifies that:
   * - One RequirementGroup is saved
   * - The correct number of required courses is stored
   * - The group is linked to the correct transcript ID
   * - The type is correctly set to "Required"
   * - The expected course codes are present in the group
   */
  @Disabled("Temporarily turned off while debugging API issues")
  @Test
  public void testSaveFromRawData_parsesAndSavesCorrectly() {
    String transcriptId = "12345L";

    // Simulate a trimmed-down version of the actual API group
    Map<String, Object> group = new HashMap<>();
    group.put("courseGroupId", 187);
    group.put("numReq", 5);
    group.put("minUnit", 0);
    group.put("name", null);

    List<Map<String, Object>> courses = new ArrayList<>();
    courses.add(Map.of("courseCode", "COMPSCI1DM3"));
    courses.add(Map.of("courseCode", "COMPSCI1JC3"));
    courses.add(Map.of("courseCode", "COMPSCI1MD3"));
    courses.add(Map.of("courseCode", "COMPSCI1XC3"));
    courses.add(Map.of("courseCode", "COMPSCI1XD3"));

    group.put("courses", courses);

    List<Map<String, Object>> rawGroups = List.of(group);

    // Call the method
    requirementGroupService.saveFromRawData(rawGroups, transcriptId);

    // Fetch and assert
    List<DataRequirementGroup> savedGroups = requirementGroupRepository.findByTranscriptId(transcriptId);
    assertEquals(1, savedGroups.size());

    DataRequirementGroup saved = savedGroups.get(0);
    assertEquals(5, saved.getNumRequired());
    assertEquals(transcriptId, saved.getTranscriptId());
    assertEquals("Required", saved.getType()); // because name is null
    assertTrue(saved.getCourses().contains("COMPSCI1MD3"));
    assertTrue(saved.getCourses().contains("COMPSCI1XD3"));
  }




  /**
 * Verifies that doesCourseRequirementExist returns true after
 * a RequirementGroup is saved with the given transcript ID.
 */
  /**
   * Verifies that doesCourseRequirementExist returns true after saving a DataRequirementGroup.
   * This test manually creates a group and persists it to the repository,
   * then checks if the service correctly detects its existence.
   */
  @Disabled("Temporarily turned off while debugging API issues")
  @Test
  public void testDoesCourseRequirementExist_returnsTrueAfterSaving() {
    String transcriptId = "testTranscript001";

    DataRequirementGroup group = new DataRequirementGroup();
    group.setCourseGroupId(123);
    group.setTranscriptId(transcriptId);
    group.setNumRequired(3);
    group.setType("Required");
    group.setCourses(List.of("COMPSCI1JC3", "COMPSCI1DM3", "COMPSCI1XC3"));

    requirementGroupRepository.save(group);

    boolean exists = requirementGroupService.doesCourseRequirementExist(transcriptId);
    assertTrue(exists, "Expected course requirements to exist after saving.");
  }

  /**
   * Integration-style test for fetchFromApiAndSave.
   * Requires the API at localhost:8080 to be running and returning valid data.
   * 
   * Verifies that the DataRequirementGroups are saved and associated with the correct transcript ID.
   */
  
  @Test
  public void testFetchFromApiAndSave_savesRequirementGroupsFromApi() {
    String transcriptId = "testTranscript002";
    String fakeProgram = "HCOMPSCI"; // must match available test API response

    requirementGroupService.fetchFromApiAndSave(fakeProgram, transcriptId);

    List<DataRequirementGroup> groups = requirementGroupRepository.findByTranscriptId(transcriptId);

    assertFalse(groups.isEmpty(), "Expected requirement groups to be saved.");
    assertEquals(transcriptId, groups.get(0).getTranscriptId());
  }


    /**
   * Tests that saveFromRawData does not insert duplicate data when a DataRequirementGroup
   * already exists for a given transcript ID.
   *
   * This test:
   * - Saves a DataRequirementGroup with a given transcript ID.
   * - Calls saveFromRawData again using the same ID.
   * - Verifies that the repository still only contains one entry (no duplicates).
   */
  @Disabled("Temporarily turned off while debugging API issues")
  @Test
  public void saveFromRawData_skipsIfTranscriptAlreadyExists() {
    String transcriptId = "dupeId";
  
    DataRequirementGroup group = new DataRequirementGroup();
    group.setCourseGroupId(123);
    group.setTranscriptId(transcriptId);
    group.setNumRequired(3);
    group.setType("Required");
    group.setCourses(List.of("COMPSCI1MD3", "COMPSCI1XD3"));
  
    // Save a dummy group to simulate already-existing data
    requirementGroupRepository.save(group);
  
    // Try saving again with different data
    Map<String, Object> dummyGroup = new HashMap<>();
    dummyGroup.put("courseGroupId", 1);
    dummyGroup.put("numReq", 3);
    dummyGroup.put("minUnit", 0);
    dummyGroup.put("name", "Electives");
  
    List<Map<String, Object>> courses = List.of(Map.of("courseCode", "TEST123"));
    dummyGroup.put("courses", courses);
  
    List<Map<String, Object>> mockedData = List.of(dummyGroup);
    requirementGroupService.saveFromRawData(mockedData, transcriptId);
  
    // Assert repository size is still 1 (nothing new added)
    List<DataRequirementGroup> allGroups = requirementGroupRepository.findByTranscriptId(transcriptId);
    assertEquals(1, allGroups.size());
  }



    /**
   * Tests the doesCourseRequirementExist method to confirm it returns false
   * when there is no data in the database for the provided transcript ID.
   *
   * Ensures that the service doesn't mistakenly report data presence when none exists.
   */

  @Disabled("Temporarily turned off while debugging API issues")
  @Test
  public void doesCourseRequirementExist_returnsFalseWhenNoData() {
    boolean exists = requirementGroupService.doesCourseRequirementExist("unknownId");
    assertFalse(exists);
  }
}
