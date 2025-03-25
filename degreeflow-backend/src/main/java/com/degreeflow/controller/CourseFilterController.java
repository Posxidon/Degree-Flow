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

    @GetMapping("/by-subject-level")
    public ResponseEntity<?> getCoursesBySubjectAndLevel(
            @RequestParam String subjectCode,
            @RequestParam String level) {

        try {
            String catalogPattern = level + "***";
            String baseUrl = "https://api.mcmaster.ca/academic-calendar/v2";
            String url = baseUrl + "/courses/wildcard-search?catalogNumberPattern=" + catalogPattern + "&subjectCode=" + subjectCode;

            String apiKey = "3da32390cf04415e91ed4feac51c9f00";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Ocp-Apim-Subscription-Key", apiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return ResponseEntity.status(response.statusCode()).body(response.body());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching courses: " + e.getMessage());
        }
    }
}