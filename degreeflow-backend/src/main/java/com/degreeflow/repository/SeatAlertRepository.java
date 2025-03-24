package com.degreeflow.repository;

import com.degreeflow.model.SeatAlertSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatAlertRepository extends JpaRepository<SeatAlertSubscription, Long> {
    List<SeatAlertSubscription> findByCourseCodeAndTerm(String courseCode, String term);
}
