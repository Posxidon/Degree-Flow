package com.degreeflow.repository;

import com.degreeflow.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    // Find a specific rating by student and course
    Optional<Rating> findByEmailAndCourseCode(String email, String courseCode);

    // Calculate average rating for a course
    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.courseCode = :courseCode")
    Double getAverageRatingForCourse(String courseCode);

    // Count total ratings for a course
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.courseCode = :courseCode")
    Integer countRatingsByCourseCode(String courseCode);
}