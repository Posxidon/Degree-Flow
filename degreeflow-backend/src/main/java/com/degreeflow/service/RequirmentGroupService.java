import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class RequirementGroupService {

    private final RequirementGroupRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RequirementGroupService(RequirementGroupRepository repository) {
        this.repository = repository;
    }

    // 🔹 Existing method
    public void saveFromRawData(List<Map<String, Object>> rawGroups, Long transcriptID) {
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

                    RequirementGroup requirementGroup = new RequirementGroup(groupId, transcriptID, numReq, type, courseCodes);

            repository.save(requirementGroup);
        }
    }

    // 🔹 NEW method to fetch from API and store it
    public void fetchFromApiAndSave(String degreeName, Long transcriptID) {
        try {
            String url = "http://localhost:8080/api/degree/requirement?degreeName=" + degreeName;

            HttpHeaders headers = new HttpHeaders();
            String auth = "user:1148626e-f576-48fa-add3-a9d9403815aa";
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            headers.set("Authorization", "Basic " + encodedAuth);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
            );

            List<Map<String, Object>> rawGroups = objectMapper.readValue(
                response.getBody(),
                new TypeReference<>() {}
            );

            saveFromRawData(rawGroups, transcriptID);

            System.out.println("Degree requirements successfully saved.");
        } catch (Exception e) {
            System.err.println("Failed to fetch and save degree requirements: " + e.getMessage());
        }
    }
}

