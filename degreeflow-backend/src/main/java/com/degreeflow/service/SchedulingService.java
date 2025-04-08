package com.degreeflow.service;

import com.degreeflow.model.JsonSchedule;
import com.degreeflow.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedulingService {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public SchedulingService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public String getLatestScheduleJsonByUserId(String userId) {
        JsonSchedule latest = scheduleRepository.findTopByUserIdOrderByIdDesc(userId);
        return latest != null ? latest.getJson() : null;
    }
}
