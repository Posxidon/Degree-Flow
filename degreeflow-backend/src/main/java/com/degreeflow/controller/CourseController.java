package com.degreeflow.controller;

import com.degreeflow.model.Course;
import com.degreeflow.service.CourseService;
import com.degreeflow.service.TimeTableScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private TimeTableScraperService timeTableScraperService;


    @GetMapping("/wildcard")
    public ResponseEntity<?> getCoursesByWildcard(
            @RequestParam String subjectCode,
            @RequestParam String catalogPattern
    ) {
        try {
            List<Map<String, Object>> courses =
                    timeTableScraperService.searchCoursesByWildcard(subjectCode, catalogPattern);

            // If no courses found, return an empty list (still 200)
            if (courses.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            // Otherwise return the data
            return ResponseEntity.ok(courses);

        } catch (IOException e) {
            // If something goes wrong, return a 500
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch wildcard courses: " + e.getMessage()));
        }
    }
}


