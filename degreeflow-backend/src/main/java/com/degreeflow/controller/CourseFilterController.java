package com.degreeflow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Course Filter",
        description = "API for filtering courses by subject and level"
)
@RestController
@RequestMapping({"/api/courses"})
@CrossOrigin(
        origins = {"*"}
)
public class CourseFilterController {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String PRIMARY_KEY = System.getenv("WILDCARD_PRIMARY_KEY");
    private static final String SECONDARY_KEY = System.getenv("WILDCARD_SECONDARY_KEY");
    private static final String baseUrl = "https://api.mcmaster.ca/academic-calendar/v2";

    public CourseFilterController() {
    }

    @Operation(
            summary = "Get courses by subject and level",
            description = "Uses the McMaster Academic Calendar API to return course results"
    )
    @GetMapping({"/by-subject-level"})
    public ResponseEntity<?> getCoursesBySubjectAndLevel(@Parameter(description = "e.g. COMP") @RequestParam String subjectCode, @Parameter(description = "e.g. 2") @RequestParam String level) {
        try {
            String catalogPattern = level + "***";
            String url = "https://api.mcmaster.ca/academic-calendar/v2/courses/wildcard-search?catalogNumberPattern=" + catalogPattern + "&subjectCode=" + subjectCode;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Ocp-Apim-Subscription-Key", PRIMARY_KEY).header("secondary-key", SECONDARY_KEY).GET().build();
            HttpResponse<String> response = this.httpClient.send(request, BodyHandlers.ofString());
            return ResponseEntity.status(response.statusCode()).body((String)response.body());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching courses: " + e.getMessage());
        }
    }
}