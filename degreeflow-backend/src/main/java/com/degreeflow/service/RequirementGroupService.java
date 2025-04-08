package com.degreeflow.service;

import com.degreeflow.model.DataRequirementGroup;
import com.degreeflow.model.Degree;
import com.degreeflow.repository.RequirementGroupRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class RequirementGroupService {

  private final PathwayService pathwayService;
  private final RequirementGroupRepository repository;
  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public RequirementGroupService(RequirementGroupRepository repository, PathwayService pathwayService) {
    this.repository = repository;
    this.pathwayService = pathwayService;
  }

  /**
   * Fetches degree requirement data from an external API based on the degree name
   * and stores it in the database, linked to the specified transcript ID.
   *
   * @param degreeName   the name of the degree used to query the API
   * @param transcriptId the student’s transcript ID to associate with the fetched data
   */
  public void fetchFromApiAndSave(String degreeName, String transcriptId) {
    Degree degree = pathwayService.parseDegreePlan(degreeName,true);
    System.out.println(degree.getName());
    JSONObject resp = new JSONObject(degree);
    System.out.println(resp);
  }

  /**
   * Parses and saves raw requirement group data retrieved from {@code fetchFromApiAndSave} into the database,
   * associating each entry with a specific transcript ID. 
   * If data for the given transcript ID already exists, the method skips insertion to avoid duplication.
   *
   * @param rawGroups    a list of raw requirement group data obtained from the degree requirement API
   * @param transcriptId the student's transcript ID used to link the data to their record
   */
  public void saveFromRawData(List<Map<String, Object>> rawGroups, String transcriptId) {
    // 🔍 Check if data for this transcript ID already exists
    if (doesCourseRequirementExist(transcriptId)) {
      System.out.println("Transcript ID " + transcriptId + " already exists. Skipping insertion.");
      return;
    }

    for (Map<String, Object> group : rawGroups) {
      Integer groupId = (Integer) group.get("courseGroupId");
      Integer numReq = (Integer) group.get("numReq");
      String name = (String) group.get("name");
      String type;

      if ("Electives".equals(name)) {
        type = "Electives";
      } else if (name != null && name.contains("Electives")) {
        type = "Technical Electives";
      } else {
        type = "Required";
      }

      @SuppressWarnings("unchecked")
      List<Map<String, Object>> courses = (List<Map<String, Object>>) group.get("courses");

      List<String> courseCodes = courses.stream()
        .map(course -> (String) course.get("courseCode"))
        .collect(Collectors.toList());

      DataRequirementGroup requirementGroup = new DataRequirementGroup(groupId, numReq, transcriptId, type, courseCodes);
      repository.save(requirementGroup);
    }
  }

  public boolean doesCourseRequirementExist(String transcriptId) {
    return repository.existsByTranscriptId(transcriptId);
  }


  public List<DataRequirementGroup> getRequirementGroupsForTranscript(String transcriptId) {
    return repository.findByTranscriptId(transcriptId);
  }
}
