package com.degreeflow.controller;

import com.degreeflow.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/schedules")  // <-- add 'public' here
@CrossOrigin(origins = "http://localhost:3000")
public class SchedulingController {
    private final SchedulingService schedulingService;

    @Autowired
    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getLatestScheduleByUserId(@PathVariable String userId) {
        String scheduleJson = schedulingService.getLatestScheduleJsonByUserId(userId);
        if (scheduleJson != null) {
            return ResponseEntity.ok(scheduleJson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
