package com.degreeflow.service;

import com.degreeflow.model.RequirementGroup;
import com.degreeflow.repository.RequirementGroupRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
public class RequirementGroupService {

    private final RequirementGroupRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient client = new OkHttpClient();
    private static final Logger logger = Logger.getLogger(RequirementGroupService.class.getName());

    // ✅ Subscription keys
    private static final String PRIMARY_KEY = "3da32390cf04415e91ed4feac51c9f00";
    private static final String SECONDARY_KEY = "f86eee675259432cb3e367128453e9b6";

    public RequirementGroupService(RequirementGroupRepository repository) {
        this.repository = repository;
    }

    /**
     * Fetches degree requirement data from API with subscription keys and saves it to the database.
     *
     * @param degreeName   the degree name to fetch requirements for
     * @param transcriptId the associated transcript ID
     */
    public void fetchFromApiAndSave(String degreeName, String transcriptId) {
        try {
            String url = "http://localhost:8080/api/degree/requirements?degreeName=" + degreeName + "&showTech=true";

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", "Mozilla/5.0")
                    .header("Ocp-Apim-Subscription-Key", PRIMARY_KEY)
                    .header("Secondary-Key", SECONDARY_KEY)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    logger.severe("Failed to fetch degree requirements: HTTP " + response.code());
                    return;
                }

                String responseBody = response.body().string();
                List<Map<String, Object>> rawGroups = objectMapper.readValue(responseBody, new TypeReference<>() {});

                saveFromRawData(rawGroups, transcriptId);
                logger.info("✅ Degree requirements saved for transcript: " + transcriptId);
            }
        } catch (IOException e) {
            logger.severe("Exception during requirement fetch: " + e.getMessage());
        }
    }

    /**
     * Saves requirement groups from raw API response into DB.
     */
    public void saveFromRawData(List<Map<String, Object>> rawGroups, String transcriptId) {
        if (doesCourseRequirementExist(transcriptId)) {
            logger.info("⛔ Requirements already exist for " + transcriptId);
            return;
        }

        for (Map<String, Object> group : rawGroups) {
            Integer groupId = (Integer) group.get("courseGroupId");
            Integer numReq = (Integer) group.get("numReq");
            String name = (String) group.get("name");

            String type = "Required";
            if ("Electives".equalsIgnoreCase(name)) {
                type = "Electives";
            } else if (name != null && name.toLowerCase().contains("elective")) {
                type = "Technical Electives";
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> courses = (List<Map<String, Object>>) group.get("courses");

            List<String> courseCodes = courses.stream()
                    .map(course -> (String) course.get("courseCode"))
                    .collect(Collectors.toList());

            RequirementGroup requirementGroup = new RequirementGroup(groupId, numReq, transcriptId, type, courseCodes);
            repository.save(requirementGroup);
        }
    }

    /**
     * Computes completion from uploaded transcript data.
     */
    public List<RequirementGroup> computeAndReturnGroups(String transcriptId) {
        List<RequirementGroup> groups = repository.findByTranscriptId(transcriptId);
        List<String> takenCourseCodes = groups.stream()
                .flatMap(g -> g.getCourses().stream())
                .map(code -> code.replaceAll("\\s", "").toUpperCase())
                .collect(Collectors.toList());

        for (RequirementGroup group : groups) {
            long completed = group.getCourses().stream()
                    .map(code -> code.replaceAll("\\s", "").toUpperCase())
                    .filter(takenCourseCodes::contains)
                    .count();

            group.setNumCompleted((int) completed);
        }

        return groups;
    }

    public boolean doesCourseRequirementExist(String transcriptId) {
        return repository.existsByTranscriptId(transcriptId);
    }
}
