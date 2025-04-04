package com.degreeflow.controller;

import com.degreeflow.model.Schedule;
import com.degreeflow.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin
public class SchedulingController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getScheduleForUser(@PathVariable String userId) {
        return scheduleRepository.findById(userId)
                .map(schedule -> ResponseEntity.ok(schedule.getScheduleJson()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
}
