package com.degreeflow.controller;

import com.degreeflow.model.Rating;
import com.degreeflow.model.RatingSummary;
import com.degreeflow.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin(origins = "*")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    /**
     * Submit a new rating or update an existing one
     */
    @PostMapping
    public ResponseEntity<?> submitRating(@RequestBody Map<String, Object> payload) {
        try {
            String email = (String) payload.get("email");
            String courseCode = (String) payload.get("courseCode");
            Integer stars = (Integer) payload.get("stars");

            if (email == null || courseCode == null || stars == null) {
                return ResponseEntity.badRequest()
                        .body("Missing required fields: email, courseCode, or stars");
            }

            Rating rating = ratingService.submitRating(email, courseCode, stars);
            return ResponseEntity.ok(rating);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while submitting the rating: " + e.getMessage());
        }
    }

    /**
     * Get a summary of ratings for a course
     */
    @GetMapping("/summary/{courseCode}")
    public ResponseEntity<RatingSummary> getRatingSummary(@PathVariable String courseCode) {
        RatingSummary summary = ratingService.getRatingSummary(courseCode);
        return ResponseEntity.ok(summary);
    }

    /**
     * Get all ratings for a specific course
     */
    @GetMapping("/course/{courseCode}")
    public ResponseEntity<List<Rating>> getRatingsByCourse(@PathVariable String courseCode) {
        List<Rating> ratings = ratingService.getRatingsByCourse(courseCode);
        return ResponseEntity.ok(ratings);
    }

    /**
     * Get all ratings submitted by a specific student
     */
    @GetMapping("/student/{email}")
    public ResponseEntity<List<Rating>> getRatingsByStudent(@PathVariable String email) {
        List<Rating> ratings = ratingService.getRatingsByStudent(email);
        return ResponseEntity.ok(ratings);
    }

    /**
     * Get a specific rating by student and course
     */
    @GetMapping("/student/{email}/course/{courseCode}")
    public ResponseEntity<?> getRatingByStudentAndCourse(
            @PathVariable String email,
            @PathVariable String courseCode) {
        Optional<Rating> rating = ratingService.getRatingByStudentAndCourse(email, courseCode);
        return rating.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a rating
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable Long id) {
        try {
            ratingService.deleteRating(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the rating: " + e.getMessage());
        }
    }
}