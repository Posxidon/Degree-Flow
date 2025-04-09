package com.degreeflow.controller;

import com.degreeflow.model.JsonSchedule;
import com.degreeflow.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "*")
public class SchedulingController {
    private final SchedulingService schedulingService;

    @Autowired
    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    @GetMapping("/getSchedule")
    public ResponseEntity<?> getLatestScheduleByUserId(@RequestParam("userID") String userID) {
        System.out.println("requested");
        JsonSchedule scheduleJson = schedulingService.getLatestScheduleJsonByUserId(userID);
        System.out.println(scheduleJson);
        if (scheduleJson!=null) {
            return ResponseEntity.ok(scheduleJson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
