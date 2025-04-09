package com.degreeflow.repository;

import com.degreeflow.model.JsonSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<JsonSchedule, Long> {
    JsonSchedule findTopByUserIdOrderByIdDesc(String userId);
}
