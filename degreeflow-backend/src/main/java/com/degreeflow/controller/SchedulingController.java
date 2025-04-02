package com.degreeflow.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin  // Allow cross-origin requests if needed
public class ScheduleController {

    @GetMapping("/{userId}")
    public ResponseEntity<?> getScheduleForUser(@PathVariable String userId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Adjust the path to where your json_schedule file is located
            File file = new File("src/main/resources/json_schedule.json");
            JsonNode root = mapper.readTree(file);

            // Assuming the file is structured with user ids as keys
            JsonNode userSchedule = root.get(userId);
            if (userSchedule == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Schedule not found for user: " + userId);
            }
            return ResponseEntity.ok(userSchedule);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading schedule: " + e.getMessage());
        }
    }
}
