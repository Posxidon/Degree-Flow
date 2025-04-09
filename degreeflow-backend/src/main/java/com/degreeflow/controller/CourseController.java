package com.degreeflow.controller;

import com.degreeflow.service.TimeTableScraperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Course Search",
        description = "Wildcard course search via Timetable Scraper"
)
@CrossOrigin(
        origins = {"*"}
)
@RestController
@RequestMapping({"/api/courses"})
public class CourseController {
    @Autowired
    private TimeTableScraperService timeTableScraperService;

    public CourseController() {
    }

    @Operation(
            summary = "Search courses by wildcard",
            description = "Search courses using subject code and catalog pattern, e.g. COMP and 2**"
    )
    @GetMapping({"/wildcard"})
    public ResponseEntity<?> getCoursesByWildcard(@Parameter(description = "e.g. COMP") @RequestParam String subjectCode, @Parameter(description = "e.g. 2**") @RequestParam String catalogPattern) {
        try {
            List<Map<String, Object>> courses = this.timeTableScraperService.searchCoursesByWildcard(subjectCode, catalogPattern);
            return courses.isEmpty() ? ResponseEntity.ok(Collections.emptyList()) : ResponseEntity.ok(courses);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to fetch wildcard courses: " + e.getMessage()));
        }
    }
}