package com.degreeflow.repository;

import com.degreeflow.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    // No additional methods are needed for basic findById()
}
