package com.degreeflow.repository;

import com.degreeflow.model.JsonSchedule;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<JsonSchedule, String> {
    Optional<JsonSchedule> findByUserId(String userId);
}


