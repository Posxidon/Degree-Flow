package com.degreeflow.repository;

import com.degreeflow.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    // findById, findAll, etc. are provided by JpaRepository
}
