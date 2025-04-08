package com.degreeflow.service;

import com.degreeflow.model.JsonSchedule;
import com.degreeflow.repository.DegreeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class SchedulingService {
    private final DegreeRepository degreeRepository;

    @Autowired
    public SchedulingService(DegreeRepository degreeRepository) {
        this.degreeRepository = degreeRepository;
//        return void;
    }

    public boolean accessToDB(String json, String userId){
        // get list of all previous records and remove them
        Optional<JsonSchedule> prevRecords = degreeRepository.findByUserId(userId);
        System.out.println("adding to db");
        System.out.println(json);
        if (prevRecords.isPresent()) {
            JsonSchedule schedule = prevRecords.get();
        }else {

        }
        return true;
    }

}
