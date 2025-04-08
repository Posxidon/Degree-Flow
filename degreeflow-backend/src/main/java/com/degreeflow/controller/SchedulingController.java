package com.degreeflow.controller;

import com.degreeflow.model.Schedule;
import com.degreeflow.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin // Allows your frontend (running on a different port) to call this endpoint
public class SchedulingController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getScheduleForUser(@PathVariable String userId) {
        return scheduleRepository.findById(userId)
                .map(schedule -> ResponseEntity.ok(schedule.getScheduleData()))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Schedule not found for user: " + userId));
    }
}