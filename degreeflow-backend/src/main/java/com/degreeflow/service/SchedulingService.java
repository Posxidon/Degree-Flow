package com.degreeflow.service;

import com.degreeflow.model.JsonSchedule;
import com.degreeflow.repository.DegreeRepository;
import com.degreeflow.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchedulingService {

    private final DegreeRepository degreeRepository;

    @Autowired
    public SchedulingService(DegreeRepository degreeRepository) {
        this.degreeRepository = degreeRepository;
    }

    public JsonSchedule getLatestScheduleJsonByUserId(String userId) {
        List<JsonSchedule> latest = degreeRepository.findByUserId(userId);
        if (latest.size()>0) {
            return latest.get(0);
        }else {
            return null;
        }
    }
}
