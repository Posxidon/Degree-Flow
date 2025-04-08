package com.degreeflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseFilterController {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    // Subscription keys
    private static final String PRIMARY_KEY = System.getenv("WILDCARD_PRIMARY_KEY");
    private static final String SECONDARY_KEY = System.getenv("WILDCARD_SECONDARY_KEY");

    private static final String baseUrl = "https://api.mcmaster.ca/academic-calendar/v2";

    @GetMapping("/by-subject-level")
    public ResponseEntity<?> getCoursesBySubjectAndLevel(
            @RequestParam String subjectCode,
            @RequestParam String level) {

        try {
            String catalogPattern = level + "***";
            String url = baseUrl + "/courses/wildcard-search?catalogNumberPattern=" + catalogPattern + "&subjectCode=" + subjectCode;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Ocp-Apim-Subscription-Key", PRIMARY_KEY)
                    .header("secondary-key", SECONDARY_KEY)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return ResponseEntity.status(response.statusCode()).body(response.body());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching courses: " + e.getMessage());
        }
    }
}